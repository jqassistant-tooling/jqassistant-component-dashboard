package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;
import org.jqassistant.tooling.dashboard.service.application.ContributorRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class ContributorRepositoryImpl extends AbstractXORepository<XOContributorRepository> implements ContributorRepository {
    ContributorRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOContributorRepository.class);
    }

    @Override
    public Stream<String> getContributors(Project project, String componentId) {
        return getXORepository().getContributors(project, componentId);
    }
}
