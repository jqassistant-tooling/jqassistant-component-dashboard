package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository {

    Project findProject(ProjectKey projectKey);

}
