package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Initializer {

    @PostConstruct
    void init() {
        // TODO Liquibase
    }

}
