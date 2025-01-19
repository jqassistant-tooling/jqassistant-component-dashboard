package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.login.LoginView;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME_SHORT;

@RequiredArgsConstructor
public class DashboardLayout extends AppLayout implements BeforeEnterObserver {

    private Class<Component> target;
    private RouteParameters routeParameters;

    @PostConstruct
    void init() {
        H3 logo = new H3(APPLICATION_NAME_SHORT);
        logo.addClassName("logo");

/*        HorizontalLayout header;
        if (getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click -> logout());
            header = new HorizontalLayout(logo, logout);
        } else {
            Button login = new Button("Login", click -> login());
            header = new HorizontalLayout(logo, login);
        }*/

        // Other page components omitted.

        addToNavbar(logo);
    }

    private void login() {
        getUI().flatMap(ui -> ui.navigate(LoginView.class)).ifPresent(loginView -> loginView.setRouteParameters(target, routeParameters));
    }

    private void logout() {
        //UI.getCurrent()
        //    .getPage()
        //    .setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent()
            .getHttpServletRequest(), null, null);
    }

    private UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication()
            .getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication()
                .getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.target = (Class<Component>) event.getNavigationTarget();
        this.routeParameters = event.getRouteParameters();
    }
}
