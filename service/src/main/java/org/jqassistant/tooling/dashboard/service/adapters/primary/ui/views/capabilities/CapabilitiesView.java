package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository.CapabilitySummary;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_TYPE;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_VALUE;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectView.*;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilitiesView extends VerticalLayout implements BeforeEnterObserver {

    private final transient CapabilityService capabilityService;

    private final TransactionTemplate transactionTemplate;

    private transient ProjectKey projectKey;

    private MultiSelectComboBox<String> typeFilterComboBox;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            this.projectKey = getProjectKey(event);
            this.typeFilterComboBox.setItems(capabilityService.getTypes(projectKey));
        });
    }

    @PostConstruct
    void init() {
        setSizeFull();

        CallbackDataProvider<CapabilitySummary, CapabilityFilter> callbackDataProvider = new CallbackDataProvider<>(
            query -> capabilityService.findAll(projectKey, query.getFilter(), query.getOffset(), query.getLimit()),
            query -> capabilityService.countAll(projectKey, query.getFilter()));
        FilterableGrid<CapabilitySummary, CapabilityFilter> filterableGrid = FilterableGrid.builder(CapabilitySummary.class, callbackDataProvider,
            new CapabilityFilter());

        // Type
        this.typeFilterComboBox = filterableGrid.multiselectComboBox(
            (capabilityFilter, typeFilter) -> capabilityFilter.setTypeFilter(typeFilter.isEmpty() ? null : typeFilter));
        filterableGrid.withColumn("Type", typeFilterComboBox, summary -> new Span(summary.getCapability()
            .getType()));

        // Value
        Component valueFilterTextComponent = filterableGrid.text(CapabilityFilter::setValueFilter);
        filterableGrid.withColumn("Value", valueFilterTextComponent, capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getValue()));

        // Description
        filterableGrid.withColumn("Description", new Span(), capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getDescription()));

        // Provided By
        filterableGrid.withColumn("Provided By", new Span(), capabilitySummary -> {
            VerticalLayout verticalLayout = new VerticalLayout();
            capabilitySummary.getProvidedByComponents()
                .stream()
                .map(component -> new Span(component.getName()))
                .forEach(verticalLayout::add);
            return verticalLayout;
        });

        Grid<CapabilitySummary> grid = filterableGrid.build();
        grid.addItemClickListener(event -> {
            Capability capability = event.getItem()
                .getCapability();
            UI.getCurrent()
                .navigate(CapabilityView.class, new RouteParam(PARAMETER_OWNER, this.projectKey.getOwner()),
                    new RouteParam(PARAMETER_PROJECT, this.projectKey.getProject()), new RouteParam(PARAMETER_CAPABILITY_TYPE, capability.getType()),
                    new RouteParam(PARAMETER_CAPABILITY_VALUE, capability.getValue()));
        });
        this.add(grid);
    }
}
