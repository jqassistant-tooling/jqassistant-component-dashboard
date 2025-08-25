package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;

    private final ComponentService componentService;

    private final ProjectService projectService;

    public Contributions resolve(ProjectKey projectKey, String componentID, String ident){
        return contributionRepository.resolveContribution(projectKey, componentID, ident);
    }


    public void setContribution(ProjectKey projectKey, String componentId, List<Contributions> contributions) {
        Component component = componentService.resolve(projectKey, componentId);
        for (Contributions contribution: contributions) {
            contribution.setToComponent(component);
        }
    }

    public Stream<ContributionSummary> getContributionSummaries(ProjectKey projectKey, String componentId) {
        Project project = projectService.find(projectKey);
        return contributionRepository.getContributionSummaries(project, componentId);
    }
}
