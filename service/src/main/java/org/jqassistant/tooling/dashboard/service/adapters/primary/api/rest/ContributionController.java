package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.ContributionMapper;
import org.jqassistant.tooling.dashboard.service.application.ContributionService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/rest/v1/{owner}/{project}/{component}/contributions")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionMapper contributionMapper;
    private final ContributionService contributionService;

    @PutMapping
    public void createOrUpdate(@PathVariable(name = "owner") String ownerId,
                               @PathVariable(name = "project") String projectId,
                               @PathVariable(name = "component") String componentId,
                               @RequestBody List<ContributionDTO> contributionDTOs) {
        System.out.println(contributionDTOs);
        ProjectKey projectKey = new ProjectKey(ownerId, projectId);
        List<Contributions> contributions = contributionDTOs.stream().map(contributionDTO -> contributionMapper.toContribution(contributionDTO, projectKey, componentId)).toList();
        contributionService.setContributions(projectKey, componentId, contributions);

    }
}
