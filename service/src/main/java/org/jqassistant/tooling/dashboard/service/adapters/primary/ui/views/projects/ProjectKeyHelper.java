package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects;

import com.vaadin.flow.router.RouteParameters;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.RouteParametersHelper;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;

import static lombok.AccessLevel.NONE;

@RequiredArgsConstructor(access = NONE)
public class ProjectKeyHelper {

    public static final String PARAMETER_OWNER = "owner";

    public static final String PARAMETER_PROJECT = "project";

    public static ProjectKey getProjectKey(RouteParameters routeParameters) {
        String owner = RouteParametersHelper.get(routeParameters, PARAMETER_OWNER);
        String project = RouteParametersHelper.get(routeParameters, PARAMETER_PROJECT);
        return new ProjectKey(owner, project);
    }

}
