package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

import java.util.stream.Stream;

import com.vaadin.flow.component.html.*;
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
import org.jqassistant.tooling.dashboard.service.application.ContributionSummary;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectKeyHelper.getProjectKey;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components/:component", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PARAMETER_COMPONENT = "component";

    private final transient ComponentService componentService;

    private final transient ContributorService contributorService;

    private final TransactionTemplate transactionTemplate;

    private final H2 title = new H2();

    private final Span url = new Span();

    private final Span description = new Span();

    private final UnorderedList contributors = new UnorderedList();

    private transient ProjectKey projectKey;

    @PostConstruct
    void init() {
        add(title, url, description, new H2("Contributors"), contributors);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            projectKey = getProjectKey(event.getRouteParameters());
            String componentId = RouteParametersHelper.get(event.getRouteParameters(), PARAMETER_COMPONENT);
            ComponentRepository.ComponentSummary componentSummary = componentService.find(projectKey, componentId);
            title.setText(componentSummary.getComponent()
                .getName());
            Version latestVersion = componentSummary.getComponent()
                .getLatestVersion();
            String latestVersionUrl = latestVersion.getUrl();
            if (latestVersionUrl != null) {
                url.add(new Anchor(latestVersionUrl, latestVersionUrl));
            }
            description.setText(latestVersion.getDescription());

            // Contributors statisch hinzufügen
            Stream<ContributionSummary> summaries = contributorService.getContributionSummaries(projectKey, componentId);
            summaries.map(summary -> {
                    Contributor contributor = summary.getContributor();
                    Contributions contributions = summary.getContributions();
                    String text = contributor.getName() + " (" + contributor.getEmail() + ")";
                    if (contributions != null) {
                        text += " – commits: " + contributions.getCommits();
                    }
                    return new ListItem(text);
                })
                .forEach(this.contributors::add);

        });
    }
}
