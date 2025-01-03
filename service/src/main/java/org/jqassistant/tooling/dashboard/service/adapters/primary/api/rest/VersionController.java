package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.ProjectService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/{version}")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VersionController {

    private final ProjectService projectService;

    private final ComponentService componentService;

    private final VersionService versionService;

    private final VersionMapper versionMapper;

    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId, @PathVariable(name = "project") String projectId,
        @PathVariable(name = "component") String componentId, @PathVariable(name = "version") String versionId, @RequestBody VersionDTO versionDTO) {
        ProjectKey projectKey = new ProjectKey(ownerId, projectId);
        createOrUpdate(projectKey, componentId, versionId, versionDTO);
    }

    private void createOrUpdate(ProjectKey projectKey, String componentId, String versionId, VersionDTO versionDTO) {
        Project project = projectService.find(projectKey);
        Component component = componentService.resolve(projectKey, componentId);
        versionService.remove(component, versionId);
        Version version = versionMapper.toVersion(project, component, versionDTO);
        log.info("Updated project '{}' component '{}' with version '{}'", projectKey, component.getName(), version.getVersion());
    }

}
