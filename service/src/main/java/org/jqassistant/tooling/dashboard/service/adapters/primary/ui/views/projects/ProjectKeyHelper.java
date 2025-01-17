package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects;

import com.vaadin.flow.router.RouteParameters;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;

import static lombok.AccessLevel.NONE;

@RequiredArgsConstructor(access = NONE)
public class ProjectKeyHelper {

    public static final String PARAMETER_OWNER = "owner";

    public static final String PARAMETER_PROJECT = "project";

    public static ProjectKey getProjectKey(RouteParameters routeParameters) {
        String owner = routeParameters.get(PARAMETER_OWNER)
            .orElseThrow();
        String project = routeParameters.get(PARAMETER_PROJECT)
            .orElseThrow();
        return new ProjectKey(owner, project);
    }

}
