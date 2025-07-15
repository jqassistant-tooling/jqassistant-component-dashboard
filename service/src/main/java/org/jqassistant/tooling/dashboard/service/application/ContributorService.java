package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
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
        log.info("Set {} contributors to component '{}' in project '{}'",
            contributors, component.getName(), projectKey);
    }

    public Stream<Contributor> getContributors(ProjectKey projectKey, String componentId) {
        Project project = projectService.find(projectKey);
        return contributorRepository.getContributors(project,componentId);
    }

}
