package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import lombok.Getter;

public class FilterableGrid<T, F> {

    private final F filter;

    private final GridDataView<T> gridDataView;
    @Getter
    private final Grid<T> grid;
    private final HeaderRow headerRow;

    public FilterableGrid(Class<T> type, CallbackDataProvider<T, F> callbackDataProvider, F filter) {
        this.filter = filter;
        this.grid = new Grid<>(type, false);
        this.grid.setSizeFull();
        this.grid.setAllRowsVisible(true);
        this.grid.getHeaderRows()
            .clear();
        ConfigurableFilterDataProvider<T, Void, F> filterDataProvider = callbackDataProvider.withConfigurableFilter();
        filterDataProvider.setFilter(filter);
        this.gridDataView = this.grid.setItems(filterDataProvider);
        this.headerRow = this.grid.appendHeaderRow();
    }

    public Grid.Column<T> addColumn(String header, Component columnHeader, SerializableFunction<T, Component> rendererFunction) {
        Grid.Column<T> column = grid.addColumn(new ComponentRenderer<>(rendererFunction));
        headerRow.getCell(column)
            .setComponent(new VerticalLayout(new NativeLabel(header), columnHeader));
        return column;
    }

    public Component text(BiConsumer<F, String> updateFilterAction) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        addValueChangeListener(textField, updateFilterAction);
        return textField;
    }

    public Component comboBox(List<String> items, BiConsumer<F, String> updateFilterAction) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        addValueChangeListener(comboBox, updateFilterAction);
        return comboBox;
    }

    public Component multiselectComboBox(List<String> items, BiConsumer<F, Set<String>> updateFilterAction) {
        MultiSelectComboBox<String> multiSelectComboBox = new MultiSelectComboBox<>();
        multiSelectComboBox.setItems(items);
        multiSelectComboBox.setClearButtonVisible(true);
        multiSelectComboBox.setWidthFull();
        addValueChangeListener(multiSelectComboBox, updateFilterAction);
        return multiSelectComboBox;
    }

    private <T> void addValueChangeListener(HasValue<?, T> hasValue, BiConsumer<F, T> updateFilterAction) {
        hasValue.addValueChangeListener(valueChangeEvent -> {
            updateFilterAction.accept(filter, valueChangeEvent.getValue());
            gridDataView.refreshAll();
        });
    }

}
