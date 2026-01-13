package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;

public interface ContributionSummary {

    Contributor getContributor();

    Contributions getContributions();
}
