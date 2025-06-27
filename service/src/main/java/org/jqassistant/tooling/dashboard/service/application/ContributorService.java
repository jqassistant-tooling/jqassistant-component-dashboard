package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service

@RequiredArgsConstructor
public class ContributorService {

    private final ProjectService projectService;

    private final ComponentService componentService;

    private final ContributorRepository contributorRepository;

    public Contributor resolve(String identString) {
        return contributorRepository.resolveContributor(identString);
    }

    public void setContributors(ProjectKey projectKey, String componentId, List<Contributor> contributors) {
        Component component = componentService.resolve(projectKey, componentId);
        for (Contributor contributor : contributors) {
               contributor.getContributedTo().add(component);
        }
    }

    public Stream<Contributor> getContributors(ProjectKey projectKey, String componentId) {
        Project project = projectService.find(projectKey);
        return contributorRepository.getContributors(project,componentId);
    }

}
