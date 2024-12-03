package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.*;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { XOConfiguration.class, XOAutoConfiguration.class })
@RequiredArgsConstructor
public class XOConfiguration {

    private final XOConfigurationProperties xoConfigurationProperties;

    private XOManagerFactory xoManagerFactory;

    @PostConstruct
    void init() throws URISyntaxException {
        migrate();
        Properties properties = new Properties();
        properties.setProperty(RemoteNeo4jXOProvider.Property.USERNAME.getKey(), xoConfigurationProperties.getUsername());
        properties.setProperty(RemoteNeo4jXOProvider.Property.PASSWORD.getKey(), xoConfigurationProperties.getPassword());
        properties.setProperty(RemoteNeo4jXOProvider.Property.ENCRYPTION.getKey(), "false");
        XOUnit xoUnit = XOUnit.builder()
            .provider(RemoteNeo4jXOProvider.class)
            .uri(new URI(xoConfigurationProperties.getUrl()))
            .properties(properties)
            .types(List.of(Owner.class, //
                Project.class, XOProjectRepository.class, //
                Component.class, XOComponentRepository.class, //
                Version.class, XOVersionRepository.class, //
                File.class, XOFileRepository.class, //
                Capability.class, XOCapabilityRepository.class))
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

    private Connection openConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "neo4j");
        properties.setProperty("password", "<redacted>");
        return new Neo4jDriver().connect("jdbc:neo4j:" + xoConfigurationProperties.getUrl(), properties);
    }
}
