package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;

public interface ContributorSummary {

    Contributor getContributor();

    Contributions getContributions();
}
