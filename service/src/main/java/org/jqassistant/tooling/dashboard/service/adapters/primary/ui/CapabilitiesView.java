package org.jqassistant.tooling.dashboard.service.adapters.primary.ui;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.StreamSupport.stream;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilitiesView extends VerticalLayout {

    private final transient CapabilityService capabilityService;

    private final Grid<Capability> grid = new Grid<>(Capability.class, false);

    private HeaderRow headerRow;

    private GridDataView<Capability> gridDataView;

    @PostConstruct
    void init() {
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.getHeaderRows()
            .clear();
        this.headerRow = grid.appendHeaderRow();

        CapabilityFilter capabilityFilter = new CapabilityFilter();
        gridDataView = grid.setItems(getDataProvider(capabilityFilter));

        // Type
        List<String> types = capabilityService.getTypes();
        Grid.Column<Capability> typeColumn = grid.addColumn(new ComponentRenderer<>(capability -> new Span(capability.getType())))
            .setHeader("Type");
        addColumnHeader(typeColumn, "Type", createComboxBoxFilterHeader(types, capabilityFilter::setTypeFilter));

        // Value
        Grid.Column<Capability> valueColumn = grid.addColumn(new ComponentRenderer<>(capability -> new Span(capability.getValue())))
            .setHeader("Value");
        addColumnHeader(valueColumn, "Value", createTextFilterHeader(capabilityFilter::setValueFilter));

        Grid.Column<Capability> descriptionColumn = grid.addColumn(new ComponentRenderer<>(capability -> new Span(capability.getDescription())))
            .setHeader("Description");
        addColumnHeader(descriptionColumn, "Description", new Span());
        this.add(grid);
    }

    private ConfigurableFilterDataProvider<Capability, Void, CapabilityFilter> getDataProvider(CapabilityFilter capabilityFilter) {
        CallbackDataProvider<Capability, CapabilityFilter> callbackDataProvider = new CallbackDataProvider<>(query -> stream(
            capabilityService.findAll(query.getFilter(), query.getOffset(), query.getLimit())
                .spliterator(), false), query -> capabilityService.countAll(query.getFilter()));
        ConfigurableFilterDataProvider<Capability, Void, CapabilityFilter> filterDataProvider = callbackDataProvider.withConfigurableFilter();
        filterDataProvider.setFilter(capabilityFilter);
        return filterDataProvider;
    }

    private Component createTextFilterHeader(Consumer<String> updateFilterAction) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addValueChangeListener(valueChangeEvent -> {
            updateFilterAction.accept(valueChangeEvent.getValue());
            gridDataView.refreshAll();
        });
        return textField;
    }

    private Component createComboxBoxFilterHeader(List<String> items, Consumer<String> updateFilterAction) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        comboBox.addValueChangeListener(valueChangeEvent -> {
            updateFilterAction.accept(valueChangeEvent.getValue());
            gridDataView.refreshAll();
        });
        return comboBox;
    }

    private void addColumnHeader(Grid.Column<Capability> column, String label, Component columnHeader) {
        headerRow.getCell(column)
            .setComponent(new VerticalLayout(new NativeLabel(label), columnHeader));
    }
}
