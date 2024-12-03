package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.ProjectRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepositoryImpl extends AbstractXORepository<XOProjectRepository> implements ProjectRepository {

    ProjectRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOProjectRepository.class);
    }

    @Override
    public Project findProject(ProjectKey projectKey) {
        return getXORepository().findProject(projectKey.getOwner(), projectKey.getOwner());
    }
}
