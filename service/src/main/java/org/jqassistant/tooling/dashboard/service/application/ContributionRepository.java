package org.jqassistant.tooling.dashboard.service.application;

import com.buschmais.xo.api.annotation.Repository;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;

@Repository
public interface ContributionRepository {

    //Stream getContributions() ?

    Contributions resolveContribution(String ident, String componentID);
}
