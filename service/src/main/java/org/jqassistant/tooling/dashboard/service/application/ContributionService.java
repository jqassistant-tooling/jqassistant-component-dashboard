package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Contribution;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;

    private final ComponentService componentService;

    public Contribution resolve(ProjectKey projectKey, String componentID, String ident){
        return contributionRepository.resolveContribution(projectKey, componentID, ident);
    }


    public void setContribution(ProjectKey projectKey, String componentId, List<Contribution> contributions) {
        Component component = componentService.resolve(projectKey, componentId);
        for (Contribution contribution: contributions) {
            contribution.setToComponent(component);
        }
    }
}
