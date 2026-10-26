package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;
import org.jqassistant.tooling.dashboard.service.application.ContributorRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.springframework.stereotype.Repository;

@Repository
public class ContributorRepositoryImpl extends AbstractXORepository<XOContributorRepository> implements ContributorRepository {

    ContributorRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOContributorRepository.class);
    }


    @Override
    public Contributor resolveContributor(String identString) {
        return getXORepository().resolveContributor(identString);
    }

}
