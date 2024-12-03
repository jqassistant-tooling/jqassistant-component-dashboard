package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project find(ProjectKey projectKey) {
        return projectRepository.findProject(projectKey);
    }

}
