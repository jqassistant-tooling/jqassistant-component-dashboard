package org.jqassistant.tooling.dashboard.service;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.configuration.XONeo4jProperties;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.config.RestApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME;
import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME_SHORT;

@EnableConfigurationProperties({ XONeo4jProperties.class, RestApiProperties.class })
@SpringBootApplication
@PWA(name = APPLICATION_NAME, shortName = APPLICATION_NAME_SHORT)
@Theme("my-theme")
public class Application implements AppShellConfigurator {

    public interface Metadata {
        String APPLICATION_NAME = "jQAssistant Component Dashboard Application";
        String APPLICATION_NAME_SHORT = "jQAssistant Component Dashboard";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
