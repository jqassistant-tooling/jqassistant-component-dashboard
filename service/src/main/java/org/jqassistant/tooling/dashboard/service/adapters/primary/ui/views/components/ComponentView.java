package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.RouteParametersHelper;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.Stream;

import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectKeyHelper.getProjectKey;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components/:component", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PARAMETER_COMPONENT = "component";

    private final transient ComponentService componentService;

    private final TransactionTemplate transactionTemplate;

    private final H2 title = new H2();

    private final Span url = new Span();

    private final Span description = new Span();

    private final VerticalLayout contributorsLayout = new VerticalLayout();

    private transient ProjectKey projectKey;

    @PostConstruct
    void init() {
        add(title, url, description, contributorsLayout);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            projectKey = getProjectKey(event.getRouteParameters());
            String componentId = RouteParametersHelper.get(event.getRouteParameters(), PARAMETER_COMPONENT);
            ComponentRepository.ComponentSummary componentSummary = componentService.find(projectKey, componentId);
            title.setText(componentSummary.getComponent().getName());
            Version latestVersion = componentSummary.getComponent().getLatestVersion();
            String latestVersionUrl = latestVersion.getUrl();
            if (latestVersionUrl != null) {
                url.add(new Anchor(latestVersionUrl, latestVersionUrl));
            }
            description.setText(latestVersion.getDescription());

            // Contributors statisch hinzufügen
            contributorsLayout.removeAll();
            contributorsLayout.add(new H2("Contributors"));
            Stream.of("Max", "Tobias", "Leonard", "Daniel", "Dirk")
                .map(name -> new Span("• " + name))
                .forEach(contributorsLayout::add);
        });
    }
}
