package org.jqassistant.tooling.dashboard.service;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME;
import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME_SHORT;

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
