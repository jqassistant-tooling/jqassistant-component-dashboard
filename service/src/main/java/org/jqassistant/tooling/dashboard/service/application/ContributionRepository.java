package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contribution;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;


public interface ContributionRepository {

    //Stream getContributions() ?

    public Contribution resolveContribution(ProjectKey projectKey, String componentID, String ident);
}
