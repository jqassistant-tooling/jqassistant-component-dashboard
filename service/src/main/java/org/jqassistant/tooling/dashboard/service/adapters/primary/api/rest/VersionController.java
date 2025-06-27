package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/versions/{version}")
@RequiredArgsConstructor
@Slf4j
public class VersionController {

    private final VersionService versionService;

    private final VersionMapper versionMapper;

    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId,
                               @PathVariable(name = "project") String projectId,
                               @PathVariable(name = "component") String componentId,
                               @PathVariable(name = "version") String versionId,
                               @RequestBody VersionDTO versionDTO) {

        ProjectKey projectKey = new ProjectKey(ownerId, projectId);

        versionService.createOrUpdate(projectKey, componentId, versionId, (project, component) -> versionMapper.toVersion(project, component, versionDTO));
    }

}
