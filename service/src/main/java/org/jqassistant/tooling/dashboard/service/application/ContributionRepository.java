package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;


public interface ContributionRepository {

    //Stream getContributions() ?

    public Contributions resolveContribution(ProjectKey projectKey, String componentID, String ident);
}
