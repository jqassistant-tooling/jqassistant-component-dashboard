package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    public Version resolve(String ownerId, String projectId, String componentId, String versionId) {
        log.info("Resolving version {}.", versionId);
        return versionRepository.resolve(ownerId, projectId, componentId, versionId);
    }

}
