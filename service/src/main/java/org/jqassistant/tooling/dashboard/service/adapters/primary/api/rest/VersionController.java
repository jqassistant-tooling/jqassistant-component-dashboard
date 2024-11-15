package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/{ownerId}/{projectId}/{componentId}/{versionId}")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VersionController {

    private final VersionMapper versionMapper;

    private final ComponentService componentService;

    private final VersionService versionService;

    @PutMapping
    public void putVersion(@PathVariable(name = "ownerId") String ownerId, @PathVariable(name = "projectId") String projectId,
        @PathVariable(name = "componentId") String componentId, @PathVariable(name = "versionId") String versionId, @RequestBody VersionDTO versionDTO) {

        Component component = componentService.resolve(ownerId, projectId, componentId);
        versionService.remove(component, versionId);
        Version version = versionMapper.toVersion(component, versionDTO);
        log.info("Updated project '{}' component '{}' with version '{}'", projectId, component.getName(), version.getVersion());
    }

}
