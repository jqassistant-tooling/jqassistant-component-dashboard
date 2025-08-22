package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributorService {

    private final ProjectService projectService;

    private final ContributorRepository contributorRepository;

    public Contributor resolve(String identString) {
        return contributorRepository.resolveContributor(identString);
    }

    public Stream<ContributorSummary> getContributorSummaries(ProjectKey projectKey, String componentId) {
        Project project = projectService.find(projectKey);
        return contributorRepository.getContributorSummaries(project, componentId);
    }
}
