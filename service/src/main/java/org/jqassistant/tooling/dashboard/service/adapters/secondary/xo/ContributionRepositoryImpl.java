package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;
import org.jqassistant.tooling.dashboard.service.application.ContributionRepository;
import org.jqassistant.tooling.dashboard.service.application.ContributionSummary;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class ContributionRepositoryImpl extends AbstractXORepository<XOContributionsRepository> implements ContributionRepository {


    ContributionRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOContributionsRepository.class);
    }

    @Override
    public Contributions resolveContribution(ProjectKey projectKey, String componentID, String ident) {
        return getXORepository().resolveContribution(projectKey.getProject(), componentID, ident);
    }

    @Override
    public Stream<ContributionSummary> getContributionSummaries(Project project, String componentId) {
        return getXORepository().getContributionSummaries(project, componentId);
    }
}
