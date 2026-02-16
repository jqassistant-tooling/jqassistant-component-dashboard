package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;

import java.util.stream.Stream;


public interface ContributionRepository {

    Contributions resolveContribution(ProjectKey projectKey, String componentID, String ident);

    Stream<ContributionSummary> getContributionSummaries(Project project, String componentId);
}
