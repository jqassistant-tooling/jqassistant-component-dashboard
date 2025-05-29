package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ContributorService {

    private final ProjectService projectService;

    private final ContributorRepository contributorRepository;


    public Stream<String> getContributors(ProjectKey projectKey, String componentId) {
        Project project = projectService.find(projectKey);
        return contributorRepository.getContributors(project,componentId);
            //Stream.of("Max", "Tobias", "Leonard", "Daniel", "Dirk");
    }
}
