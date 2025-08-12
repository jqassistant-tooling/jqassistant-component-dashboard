package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ContributorRepository {

    Stream<Contributor> getContributors(Project project, String componentId);

    Contributor resolveContributor(String identString);

    Stream<ContributorSummary> getContributorSummaries(Project project, String componentId);
}
