package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VersionService {

    private final ProjectService projectService;

    private final ComponentService componentService;

    private final VersionRepository versionRepository;

    public Version resolve(Component component, String version) {
        return versionRepository.create(component, version);
    }

    public void createOrUpdate(ProjectKey projectKey, String componentId, String versionId, EntitySupplier<Version> versionSupplier) {
        Project project = projectService.find(projectKey);
        Component component = componentService.resolve(projectKey, componentId);
        versionRepository.remove(component, versionId);
        Version version = versionSupplier.get(project, component);
        Version latestVersion = component.getLatestVersion();
        if (latestVersion == null || latestVersion.getUpdatedAt()
            .isBefore(version.getUpdatedAt())) {
            component.setLatestVersion(version);
        }
        log.info("Updated project '{}' component '{}' with version '{}'", projectKey, component.getName(), version.getVersion());
    }

}
