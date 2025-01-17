package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.security.core.userdetails.User;

@RoutePrefix("ui")
@Route(value = ":owner/:project", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
public class ProjectView extends VerticalLayout implements BeforeEnterObserver {

    private final transient AuthenticationContext authenticationContext;

    @PostConstruct
    void init() {
        add(new Text("Components"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println(authenticationContext.getAuthenticatedUser(User.class));
    }

}
