package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import static org.jqassistant.tooling.dashboard.service.Application.Metadata.APPLICATION_NAME_SHORT;

@RoutePrefix("ui")
@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    private Class<Component> target;
    private RouteParameters routeParameters;

    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.addLoginListener(event -> {
            System.out.println("Login event: " + event);
            UI.getCurrent()
                .navigate(target, routeParameters);
        });

        add(new H1(APPLICATION_NAME_SHORT), login);
    }

    public void setRouteParameters(Class<Component> target, RouteParameters routeParameters) {
        this.target = target;
        this.routeParameters = routeParameters;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            login.setError(true);
        }
    }

}
