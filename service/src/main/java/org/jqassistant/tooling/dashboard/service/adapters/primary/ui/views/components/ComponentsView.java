package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

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
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.StreamSupport.stream;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components.ComponentView.PARAMETER_COMPONENT;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectView.*;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentsView extends VerticalLayout implements BeforeEnterObserver {

    private final ComponentService componentService;

    private ProjectKey projectKey;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.projectKey = getProjectKey(event);
    }

    @PostConstruct
    void init() {
        FilterableGrid<Component, ComponentFilter> filterableGrid = FilterableGrid.builder(Component.class,
            new CallbackDataProvider<Component, ComponentFilter>(query -> stream(
                componentService.findAll(projectKey, query.getFilter(), query.getOffset(), query.getLimit())
                    .spliterator(), false), query -> componentService.countAll(projectKey, query.getFilter())), new ComponentFilter());

        // Name
        com.vaadin.flow.component.Component nameFilterTextBox = filterableGrid.text(ComponentFilter::setNameFilter);
        filterableGrid.withColumn("Name", nameFilterTextBox, component -> new Span(component.getName()));

        Grid<Component> grid = filterableGrid.build();
        grid.addItemClickListener(event -> UI.getCurrent()
            .navigate(ComponentView.class, new RouteParam(PARAMETER_OWNER, this.projectKey.getOwner()),
                new RouteParam(PARAMETER_PROJECT, this.projectKey.getProject()), new RouteParam(PARAMETER_COMPONENT, event.getItem()
                    .getName())));
        this.add(grid);
    }

}
