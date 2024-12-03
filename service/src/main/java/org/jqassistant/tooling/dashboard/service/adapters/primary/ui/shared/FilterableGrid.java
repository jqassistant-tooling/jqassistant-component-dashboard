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

import static com.vaadin.flow.component.grid.GridVariant.LUMO_ROW_STRIPES;
import static com.vaadin.flow.component.grid.GridVariant.LUMO_WRAP_CELL_CONTENT;

public class FilterableGrid<T, F> {

    private final F filter;

    private final GridDataView<T> gridDataView;
    private final Grid<T> grid;
    private final HeaderRow headerRow;

    public static <T, F> FilterableGrid<T, F> builder(Class<T> type, CallbackDataProvider<T, F> callbackDataProvider, F filter) {
        return new FilterableGrid<>(type, callbackDataProvider, filter);
    }

    private FilterableGrid(Class<T> type, CallbackDataProvider<T, F> callbackDataProvider, F filter) {
        this.filter = filter;
        this.grid = new Grid<>(type, false);
        this.grid.setSizeFull();
        this.grid.setAllRowsVisible(true);
        this.grid.getHeaderRows()
            .clear();
        grid.addThemeVariants(LUMO_WRAP_CELL_CONTENT, LUMO_ROW_STRIPES);
        ConfigurableFilterDataProvider<T, Void, F> filterDataProvider = callbackDataProvider.withConfigurableFilter();
        filterDataProvider.setFilter(filter);
        this.gridDataView = this.grid.setItems(filterDataProvider);
        this.headerRow = this.grid.appendHeaderRow();
    }

    public Grid.Column<T> withColumn(String header, Component columnHeader, SerializableFunction<T, Component> rendererFunction) {
        Grid.Column<T> column = grid.addColumn(new ComponentRenderer<>(rendererFunction));
        headerRow.getCell(column)
            .setComponent(new VerticalLayout(new NativeLabel(header), columnHeader));
        return column;
    }

    public TextField text(BiConsumer<F, String> updateFilterAction) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        addValueChangeListener(textField, updateFilterAction);
        return textField;
    }

    public ComboBox comboBox(List<String> items, BiConsumer<F, String> updateFilterAction) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        addValueChangeListener(comboBox, updateFilterAction);
        return comboBox;
    }

    public MultiSelectComboBox<String> multiselectComboBox(BiConsumer<F, Set<String>> updateFilterAction) {
        MultiSelectComboBox<String> multiSelectComboBox = new MultiSelectComboBox<>();
        multiSelectComboBox.setClearButtonVisible(true);
        multiSelectComboBox.setWidthFull();
        addValueChangeListener(multiSelectComboBox, updateFilterAction);
        return multiSelectComboBox;
    }

    private <V> void addValueChangeListener(HasValue<?, V> hasValue, BiConsumer<F, V> updateFilterAction) {
        hasValue.addValueChangeListener(valueChangeEvent -> {
            updateFilterAction.accept(filter, valueChangeEvent.getValue());
            gridDataView.refreshAll();
        });
    }

    public Grid<T> build() {
        return grid;
    }
}
