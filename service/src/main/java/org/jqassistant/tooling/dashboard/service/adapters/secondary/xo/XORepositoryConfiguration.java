package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.FileRepository;
import org.jqassistant.tooling.dashboard.service.application.VersionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@RequiredArgsConstructor
public class XORepositoryConfiguration {

    private final XOManager xoManager;

    @Bean
    @RequestScope
    ComponentRepository componentRepository() {
        return xoManager.getRepository(XOComponentRepository.class);
    }

    @Bean
    @RequestScope
    VersionRepository versionRepository() {
        return xoManager.getRepository(XOVersionRepository.class);
    }

    @Bean
    @RequestScope
    FileRepository fileRepository() {
        return xoManager.getRepository(XOFileRepository.class);
    }

}
