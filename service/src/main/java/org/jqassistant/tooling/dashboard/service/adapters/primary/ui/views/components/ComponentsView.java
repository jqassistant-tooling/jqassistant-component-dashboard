package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.FilterableGrid;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository.ComponentSummary;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.stream.StreamSupport.stream;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components.ComponentView.PARAMETER_COMPONENT;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectView.*;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentsView extends VerticalLayout implements BeforeEnterObserver {

    private final transient ComponentService componentService;

    private transient ProjectKey projectKey;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.projectKey = getProjectKey(event);
    }

    @PostConstruct
    void init() {
        setSizeFull();

        FilterableGrid<ComponentSummary, ComponentFilter> filterableGrid = FilterableGrid.builder(ComponentSummary.class,
            new CallbackDataProvider<ComponentSummary, ComponentFilter>(query -> stream(
                componentService.findAll(projectKey, query.getFilter(), query.getOffset(), query.getLimit())
                    .spliterator(), false), query -> componentService.countAll(projectKey, query.getFilter())), new ComponentFilter());

        // Name
        Component nameFilterTextBox = filterableGrid.text("Name", ComponentFilter::setNameFilter);
        filterableGrid.withColumn(nameFilterTextBox, componentSummary -> new Span(componentSummary.getLatestVersion()
            .getName()));
        // Description
        Component descriptionFilterTextBox = filterableGrid.text("Description", ComponentFilter::setNameFilter);
        filterableGrid.withColumn(descriptionFilterTextBox, componentSummary -> new Span(componentSummary.getLatestVersion()
            .getDescription()));
        // Latest Version
        filterableGrid.withColumn("Latest Version", componentSummary -> new Span(componentSummary.getLatestVersion()
            .getVersion()));
        // Latest Version
        filterableGrid.withColumn("Updated", componentSummary -> new Span(componentSummary.getLatestVersion()
            .getUpdatedAt()
            .format(ISO_LOCAL_DATE_TIME)));
        // Version #
        filterableGrid.withColumn("Version #", componentSummary -> new Span(Long.toString(componentSummary.getVersionCount())));

        Grid<ComponentSummary> grid = filterableGrid.build();
        grid.addItemClickListener(event -> UI.getCurrent()
            .navigate(ComponentView.class, new RouteParam(PARAMETER_OWNER, this.projectKey.getOwner()),
                new RouteParam(PARAMETER_PROJECT, this.projectKey.getProject()), new RouteParam(PARAMETER_COMPONENT, event.getItem()
                    .getComponent()
                    .getName())));
        this.add(grid);
    }

}
