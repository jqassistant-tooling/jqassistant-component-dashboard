package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/{ownerId}/{projectId}/{componentId}/{versionId}")
@RequiredArgsConstructor
@Transactional
public class VersionController {

    private final VersionMapper versionMapper;

    private final VersionService versionService;

    @PutMapping
    public void putVersion(@PathVariable(name = "ownerId") String ownerId, @PathVariable(name = "projectId") String projectId,
        @PathVariable(name = "componentId") String componentId, @PathVariable(name = "versionId") String versionId, @RequestBody VersionDTO versionDTO) {

        Version version = versionMapper.toVersion(versionDTO, versionService);

        System.out.println(versionDTO + ":" + version);
    }

}
