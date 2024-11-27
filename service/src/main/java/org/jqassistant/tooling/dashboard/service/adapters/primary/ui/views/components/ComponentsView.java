package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.FilterableGrid;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.StreamSupport.stream;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentsView extends VerticalLayout {

    private final transient ComponentService componentService;

    @PostConstruct
    void init() {
        FilterableGrid<Component, ComponentFilter> filterableGrid = new FilterableGrid<>(Component.class, new CallbackDataProvider<Component, ComponentFilter>(
            query -> stream(componentService.findAll(query.getFilter(), query.getOffset(), query.getLimit())
                .spliterator(), false), query -> componentService.countAll(query.getFilter())), new ComponentFilter());

        // Name
        com.vaadin.flow.component.Component nameFilterTextBox = filterableGrid.text((componentFilter, nameFilter) -> componentFilter.setNameFilter(nameFilter));
        filterableGrid.addColumn("Name", nameFilterTextBox, component -> new Span(component.getName()));

        this.add(filterableGrid.getGrid());
    }
}
