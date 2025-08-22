package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.impl.XOManagerFactoryImpl;
import com.buschmais.xo.neo4j.remote.api.RemoteNeo4jXOProvider;
import com.buschmais.xo.spring.XOAutoConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.ext.neo4j.database.jdbc.Neo4jDriver;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.*;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { XOConfiguration.class, XOAutoConfiguration.class })
@RequiredArgsConstructor
public class XOConfiguration {

    private final XONeo4jProperties xoNeo4jProperties;

    private XOManagerFactory xoManagerFactory;

    @PostConstruct
    void init() throws URISyntaxException {
        if (xoNeo4jProperties.getUrl() == null) {
            throw new IllegalStateException("Neo4j URL is not configured, please provide a valid configuration as application.properties or environment variable(s).");
        }
        log.info("Connecting to '{}@{}'.", xoNeo4jProperties.getUsername(), xoNeo4jProperties.getUrl());
        migrate();
        Properties properties = new Properties();
        properties.setProperty(RemoteNeo4jXOProvider.Property.USERNAME.getKey(), xoNeo4jProperties.getUsername());
        properties.setProperty(RemoteNeo4jXOProvider.Property.PASSWORD.getKey(), xoNeo4jProperties.getPassword());
        properties.setProperty(RemoteNeo4jXOProvider.Property.ENCRYPTION.getKey(), "false");
        XOUnit xoUnit = XOUnit.builder()
            .provider(RemoteNeo4jXOProvider.class)
            .uri(new URI(xoNeo4jProperties.getUrl()))
            .properties(properties)
            .types(List.of(Owner.class, //
                Project.class, XOProjectRepository.class, //
                Component.class, XOComponentRepository.class, //
                Version.class, XOVersionRepository.class, //
                File.class, XOFileRepository.class, //
                Capability.class, XOCapabilityRepository.class, //
                Contributor.class, XOContributorRepository.class,
                Contribution.class, XOContributionsRepository.class))
            .build();
        this.xoManagerFactory = new XOManagerFactoryImpl<>(xoUnit);
    }

    @PreDestroy
    void destroy() {
        xoManagerFactory.close();
    }

    @Bean
    public XOManagerFactory getXOManagerFactory() {
        return xoManagerFactory;
    }

    private void migrate() {
        try {
            Connection connection = openConnection(); //your openConnection logic
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
                CommandScope update = new CommandScope("update");
                update.addArgumentValue("changelogFile", "db/changelog/changelog.xml");
                update.addArgumentValue("database", database);

                update.execute();
            });
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Connection openConnection() {
        Properties properties = new Properties();
        properties.setProperty("user", xoNeo4jProperties.getUsername());
        properties.setProperty("password", xoNeo4jProperties.getPassword());
        properties.setProperty("encryption", "false");
        return new Neo4jDriver().connect("jdbc:neo4j:" + xoNeo4jProperties.getUrl(), properties);
    }
}
