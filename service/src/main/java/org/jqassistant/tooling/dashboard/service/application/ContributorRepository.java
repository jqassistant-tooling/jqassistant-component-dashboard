package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ContributorRepository {

    Contributor resolveContributor(String identString);

    Stream<ContributionSummary> getContributionSummaries(Project project, String componentId);
}
