package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import org.jqassistant.tooling.dashboard.service.application.ContributorRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class ContributorRepositoryImpl implements ContributorRepository {
    @Override
    public Stream<String> getContributors(Project project, String componentId) {
        return Stream.of("Max", "Tobias", "Leonard", "Daniel", "Dirk");
    }
}
