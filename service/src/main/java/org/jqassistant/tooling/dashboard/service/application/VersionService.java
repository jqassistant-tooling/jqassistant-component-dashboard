package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    public void remove(Component component, String version) {
        versionRepository.remove(component, version);
    }

    public Version resolve(Component component, String version) {
        return versionRepository.create(component, version);
    }

}
