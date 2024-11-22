package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
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

    private final Grid<CapabilitySummary> grid = new Grid<>(CapabilitySummary.class, false);

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
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.getHeaderRows()
            .clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        CapabilityFilter capabilityFilter = new CapabilityFilter();
        gridDataView = grid.setItems(getDataProvider(capabilityFilter));

        // Type
        List<String> types = capabilityService.getTypes();
        Grid.Column<CapabilitySummary> typeColumn = grid.addColumn(new ComponentRenderer<>(capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getType())));
        addColumnHeader(headerRow, typeColumn, "Type",
            createMultiselectComboxBoxFilter(types, typeFilter -> capabilityFilter.setTypeFilter(typeFilter.isEmpty() ? null : typeFilter)));

        // Value
        Grid.Column<CapabilitySummary> valueColumn = grid.addColumn(new ComponentRenderer<>(capabilitySummary -> new Span(capabilitySummary.getCapability()
            .getValue())));
        addColumnHeader(headerRow, valueColumn, "Value", createTextFilter(capabilityFilter::setValueFilter));

        Grid.Column<CapabilitySummary> descriptionColumn = grid.addColumn(new ComponentRenderer<>(capabilitySummary -> new Span(
            capabilitySummary.getCapability()
                .getDescription())));
        addColumnHeader(headerRow, descriptionColumn, "Description", new Span());

        Grid.Column<CapabilitySummary> providedByComponentsColumn = grid.addColumn(new ComponentRenderer<>(capabilitySummary -> {
            VerticalLayout verticalLayout = new VerticalLayout();
            capabilitySummary.getProvidedByComponents()
                .stream()
                .map(component -> new Span(component.getName()))
                .forEach(verticalLayout::add);
            return verticalLayout;
        }));
        addColumnHeader(headerRow, providedByComponentsColumn, "Provided By", new Span());
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

    private ConfigurableFilterDataProvider<CapabilitySummary, Void, CapabilityFilter> getDataProvider(CapabilityFilter capabilityFilter) {
        CallbackDataProvider<CapabilitySummary, CapabilityFilter> callbackDataProvider = new CallbackDataProvider<>(
            query -> capabilityService.findAll(query.getFilter(), query.getOffset(), query.getLimit()), query -> capabilityService.countAll(query.getFilter()));
        ConfigurableFilterDataProvider<CapabilitySummary, Void, CapabilityFilter> filterDataProvider = callbackDataProvider.withConfigurableFilter();
        filterDataProvider.setFilter(capabilityFilter);
        return filterDataProvider;
    }

    private Component createTextFilter(Consumer<String> updateFilterAction) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        addValueChangeListener(textField, updateFilterAction);
        return textField;
    }

    private Component createComboBoxFilter(List<String> items, Consumer<String> updateFilterAction) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        addValueChangeListener(comboBox, updateFilterAction);
        return comboBox;
    }

    private Component createMultiselectComboxBoxFilter(List<String> items, Consumer<Set<String>> updateFilterAction) {
        MultiSelectComboBox<String> multiSelectComboBox = new MultiSelectComboBox<>();
        multiSelectComboBox.setItems(items);
        multiSelectComboBox.setClearButtonVisible(true);
        multiSelectComboBox.setWidthFull();
        addValueChangeListener(multiSelectComboBox, updateFilterAction);
        return multiSelectComboBox;
    }

    private <T> void addValueChangeListener(HasValue<?, T> hasValue, Consumer<T> updateFilterAction) {
        hasValue.addValueChangeListener(valueChangeEvent -> {
            updateFilterAction.accept(valueChangeEvent.getValue());
            gridDataView.refreshAll();
        });
    }

    private void addColumnHeader(HeaderRow headerRow, Grid.Column<CapabilitySummary> column, String label, Component columnHeader) {
        headerRow.getCell(column)
            .setComponent(new VerticalLayout(new NativeLabel(label), columnHeader));
    }

}
