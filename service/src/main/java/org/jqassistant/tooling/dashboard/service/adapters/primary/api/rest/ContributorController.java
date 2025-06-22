package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.ContributorMapper;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/contributors")
@RequiredArgsConstructor
@Slf4j
public class ContributorController {

    private final ContributorService contributorService;
    private final ContributorMapper contributorMapper;
    private ContributorDTO ContributorDTO;

    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId, @PathVariable(name = "project") String projectId, @PathVariable(name = "component") String componentId,
                               @RequestBody VersionDTO versionDTO) {
        ProjectKey projectKey = new ProjectKey(ownerId, projectId);

        contributorService.setContributors(projectKey, componentId, (project, component) -> contributorMapper.contributedto(project, component, ContributorDTO);


    }
}
