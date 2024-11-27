package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.FilterableGrid;
import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository.CapabilitySummary;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.springframework.transaction.annotation.Transactional;

import static com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES;
import static com.vaadin.flow.component.grid.GridVariant.LUMO_WRAP_CELL_CONTENT;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_TYPE;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_VALUE;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilitiesView extends VerticalLayout implements BeforeEnterObserver {

    private final transient CapabilityService capabilityService;

    private String owner;

    private String project;

    private GridDataView<CapabilitySummary> gridDataView;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.owner = event.getRouteParameters()
            .get("owner")
            .orElse(null);
        this.project = event.getRouteParameters()
            .get("project")
            .orElse(null);
    }

    @PostConstruct
    void init() {
        CallbackDataProvider<CapabilitySummary, CapabilityFilter> callbackDataProvider = new CallbackDataProvider<>(
            query -> capabilityService.findAll(query.getFilter(), query.getOffset(), query.getLimit()), query -> capabilityService.countAll(query.getFilter()));
        FilterableGrid<CapabilitySummary, CapabilityFilter> filterableGrid = new FilterableGrid<>(CapabilitySummary.class, callbackDataProvider,
            new CapabilityFilter());

        // Type
        Component typeFilterComboBox = filterableGrid.multiselectComboBox(capabilityService.getTypes(),
            (capabilityFilter, typeFilter) -> capabilityFilter.setTypeFilter(typeFilter.isEmpty() ? null : typeFilter));
        filterableGrid.addColumn("Type", typeFilterComboBox, summary -> new Span(summary.getCapability()
            .getType()));

        // Value
        Component valueFilterTextComponent = filterableGrid.text((capabilityFilter, valueFilter) -> capabilityFilter.setValueFilter(valueFilter));
        filterableGrid.addColumn("Value", valueFilterTextComponent, capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getValue()));

        // Description
        filterableGrid.addColumn("Description", new Span(), capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getDescription()));

        // Provided By
        filterableGrid.addColumn("Provided By", new Span(), capabilitySummary -> {
            VerticalLayout verticalLayout = new VerticalLayout();
            capabilitySummary.getProvidedByComponents()
                .stream()
                .map(component -> new Span(component.getName()))
                .forEach(verticalLayout::add);
            return verticalLayout;
        });

        Grid<CapabilitySummary> grid = filterableGrid.getGrid();
        grid.addThemeVariants(LUMO_WRAP_CELL_CONTENT, LUMO_ROW_STRIPES);
        grid.addItemClickListener(event -> {
            Capability capability = event.getItem()
                .getCapability();
            UI.getCurrent()
                .navigate(CapabilityView.class, new RouteParam("owner", this.owner), new RouteParam("project", this.project),
                    new RouteParam(PARAMETER_CAPABILITY_TYPE, capability.getType()), new RouteParam(PARAMETER_CAPABILITY_VALUE, capability.getValue()));
        });
        this.add(grid);
    }
}
