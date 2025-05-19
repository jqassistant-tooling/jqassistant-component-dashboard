package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/contributors")
@RequiredArgsConstructor
@Slf4j
public class ContributorController {



    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId, @PathVariable(name = "project") String projectId,
        @PathVariable(name = "component") String componentId,  @RequestBody List<ContributorDTO> contributorDTOS) {

    }

}
