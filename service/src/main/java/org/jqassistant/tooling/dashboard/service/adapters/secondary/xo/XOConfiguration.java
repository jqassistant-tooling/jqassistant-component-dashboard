package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.impl.XOManagerFactoryImpl;
import com.buschmais.xo.neo4j.remote.api.RemoteNeo4jXOProvider;
import com.buschmais.xo.spring.XOAutoConfiguration;
import com.buschmais.xo.spring.XOTransactionManager;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Owner;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { XOConfiguration.class, XOAutoConfiguration.class, XOTransactionManager.class })
public class XOConfiguration {

    private static final String NEO4J_URL = "bolt://localhost:7687";

    private XOManagerFactory xoManagerFactory;

    @PostConstruct
    void init() throws URISyntaxException {
        Properties properties = new Properties();
        properties.setProperty(RemoteNeo4jXOProvider.Property.ENCRYPTION.getKey(), "false");
        XOUnit xoUnit = XOUnit.builder()
            .provider(RemoteNeo4jXOProvider.class)
            .uri(new URI(NEO4J_URL))
            .properties(properties)
            .types(List.of(Owner.class, Project.class, Component.class, Version.class))
            .build();
        this.xoManagerFactory = new XOManagerFactoryImpl(xoUnit);
    }

    @PreDestroy
    void destroy() {
        xoManagerFactory.close();
    }

    @Bean
    public XOManagerFactory getXOManagerFactory() {
        return xoManagerFactory;
    }

}
