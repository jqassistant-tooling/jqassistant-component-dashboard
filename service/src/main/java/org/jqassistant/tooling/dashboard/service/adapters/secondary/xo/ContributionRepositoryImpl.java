package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;
import org.jqassistant.tooling.dashboard.service.application.ContributionRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.springframework.stereotype.Repository;

@Repository
public class ContributionRepositoryImpl extends AbstractXORepository<XOContributionsRepository> implements ContributionRepository {


    ContributionRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOContributionsRepository.class);
    }

    @Override
    public Contributions resolveContribution(String ident, String componentID) {
        return getXORepository().resolveContribution(ident, componentID);
    }
}
