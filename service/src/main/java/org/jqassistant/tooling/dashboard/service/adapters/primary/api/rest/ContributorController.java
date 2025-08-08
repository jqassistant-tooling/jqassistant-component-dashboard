package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.ContributorMapper;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/contributors")
@RequiredArgsConstructor
public class ContributorController {

    private final ContributorService contributorService;
    private final ContributorMapper contributorMapper;

    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId,
                               @PathVariable(name = "project") String projectId,
                               @PathVariable(name = "component") String componentId,
                               @RequestBody List<ContributionDTO> contributionDTOs) {

        ProjectKey projectKey = new ProjectKey(ownerId, projectId);




        //List<Contributor> contributors = contributorDTOs.stream().map(contributorDTO -> contributorMapper.toContributor(contributorDTO)).toList();
        List<Contributor> contributors = Collections.emptyList();

        contributorService.setContributors(projectKey, componentId, contributors);

    }
}
