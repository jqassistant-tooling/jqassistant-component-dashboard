package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.FilterableGrid;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.QueryParamsHelper;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.RouteParametersHelper;
import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository.CapabilitySummary;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static java.util.Set.copyOf;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.QueryParamsHelper.join;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.QueryParamsHelper.split;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_TYPE;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities.CapabilityView.PARAMETER_CAPABILITY_VALUE;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectKeyHelper.*;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilitiesView extends VerticalLayout implements BeforeEnterObserver {

    public static final String QUERY_PARAM_TYPE_FILTER = "typeFilter";
    public static final String QUERY_PARAM_VALUE_FILTER = "valueFilter";
    private final transient CapabilityService capabilityService;

    private final TransactionTemplate transactionTemplate;

    private final transient CapabilityFilter capabilityFilter = new CapabilityFilter();

    private final Binder<CapabilityFilter> filterBinder = new Binder<>(CapabilityFilter.class);

    private transient QueryParamsHelper queryParamsHelper;

    private transient ProjectKey projectKey;

    private MultiSelectComboBox<String> typeFilterComboBox;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            this.projectKey = getProjectKey(event.getRouteParameters());
            this.typeFilterComboBox.setItems(capabilityService.getTypes(projectKey));
        });
        queryParamsHelper = new QueryParamsHelper(event.getLocation()).withParameters(QUERY_PARAM_TYPE_FILTER,
                typeFilter -> capabilityFilter.setTypeFilter(copyOf(typeFilter)))
            .withParameter(QUERY_PARAM_VALUE_FILTER, valueFilter -> capabilityFilter.setValueFilter(split(valueFilter)));
        filterBinder.readBean(capabilityFilter);
    }

    @PostConstruct
    void init() {
        setSizeFull();

        CallbackDataProvider<CapabilitySummary, CapabilityFilter> callbackDataProvider = new CallbackDataProvider<>(
            query -> capabilityService.findAll(projectKey, query.getFilter(), query.getOffset(), query.getLimit()),
            query -> capabilityService.countAll(projectKey, query.getFilter()));
        FilterableGrid<CapabilitySummary, CapabilityFilter> filterableGrid = FilterableGrid.builder(CapabilitySummary.class, callbackDataProvider,
            capabilityFilter);

        // Type
        this.typeFilterComboBox = filterableGrid.multiselectComboBox("Type",
            (filter, typeFilter) -> filter.setTypeFilter(typeFilter.isEmpty() ? null : typeFilter));
        filterableGrid.withColumn(typeFilterComboBox, summary -> new Span(summary.getCapability()
            .getType()));

        // Value
        TextField valueFilterTextField = filterableGrid.text("Value", (filter, valueFilter) -> filter.setValueFilter(split(valueFilter)));
        filterableGrid.withColumn(valueFilterTextField, capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getValue()));

        // Description
        filterableGrid.withColumn(new Span(), capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getDescription()));

        // Provided By
        filterableGrid.withColumn(new Span(), capabilitySummary -> {
            VerticalLayout verticalLayout = new VerticalLayout();
            capabilitySummary.getProvidedByComponents()
                .stream()
                .map(component -> new Span(component.getName()))
                .forEach(verticalLayout::add);
            return verticalLayout;
        });

        filterBinder.forField(typeFilterComboBox)
            .bind(CapabilityFilter::getTypeFilter, CapabilityFilter::setTypeFilter);
        filterBinder.forField(valueFilterTextField)
            .bind(filter -> join(filter.getValueFilter()), (filter, valueFilter) -> filter.setValueFilter(split(valueFilter)));
        filterableGrid.addFilterListener(filter -> queryParamsHelper.update(getUI(), uriBuilder -> {
            if (isNotEmpty(capabilityFilter.getTypeFilter())) {
                uriBuilder.queryParam(QUERY_PARAM_TYPE_FILTER, capabilityFilter.getTypeFilter());
            }
            if (isNotEmpty(capabilityFilter.getValueFilter())) {
                uriBuilder.queryParam(QUERY_PARAM_VALUE_FILTER, join(capabilityFilter.getValueFilter()));
            }
        }));

        Grid<CapabilitySummary> grid = filterableGrid.build();

        grid.addItemClickListener(event -> {
            Capability capability = event.getItem()
                .getCapability();
            UI.getCurrent()
                .navigate(CapabilityView.class, RouteParametersHelper.builder()
                    .withParameter(PARAMETER_OWNER, this.projectKey.getOwner())
                    .withParameter(PARAMETER_PROJECT, this.projectKey.getProject())
                    .withParameter(PARAMETER_CAPABILITY_TYPE, capability.getType())
                    .withParameter(PARAMETER_CAPABILITY_VALUE, capability.getValue())
                    .build());
        });
        this.add(grid);
    }
}
