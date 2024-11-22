package org.jqassistant.tooling.dashboard.service.adapters.primary.ui;

import java.util.function.Consumer;

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

    private final Grid<Component> grid = new Grid<>(Component.class, false);

    private HeaderRow headerRow;

    private GridDataView<Component> gridDataView;

    @PostConstruct
    void init() {
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.getHeaderRows()
            .clear();
        this.headerRow = grid.appendHeaderRow();

        ComponentFilter componentFilter = new ComponentFilter();
        gridDataView = grid.setItems(getDataProvider(componentFilter));
        

        // Name
        Grid.Column<Component> typeColumn = grid.addColumn(new ComponentRenderer<>(component -> new Span(component.getName())))
            .setHeader("Name");
        addFilterHeader(typeColumn, "Name", componentFilter::setNameFilter);

        this.add(grid);
    }

    private ConfigurableFilterDataProvider<Component, Void, ComponentFilter> getDataProvider(ComponentFilter componentFilter) {
        CallbackDataProvider<Component, ComponentFilter> callbackDataProvider = new CallbackDataProvider<>(query -> stream(
            componentService.findAll(query.getFilter(), query.getOffset(), query.getLimit())
                .spliterator(), false), query -> componentService.countAll(query.getFilter()));
        ConfigurableFilterDataProvider<Component, Void, ComponentFilter> filterDataProvider = callbackDataProvider.withConfigurableFilter();
        filterDataProvider.setFilter(componentFilter);
        return filterDataProvider;
    }

    private void addFilterHeader(Grid.Column<Component> column, String label, Consumer<String> updateFilterAction) {
        com.vaadin.flow.component.Component filterHeader = createFilterHeader(label, valueFilter -> {
            updateFilterAction.accept(valueFilter);
            gridDataView.refreshAll();
        });
        headerRow.getCell(column)
            .setComponent(filterHeader);
    }

    private static com.vaadin.flow.component.Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
        return new VerticalLayout(new NativeLabel(labelText), textField);
    }

}
