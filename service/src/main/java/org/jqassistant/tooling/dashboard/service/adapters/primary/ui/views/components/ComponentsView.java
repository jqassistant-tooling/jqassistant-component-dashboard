package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.FilterableGrid;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.QueryParamsHelper;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository.ComponentSummary;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.transaction.annotation.Transactional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.components.ComponentView.PARAMETER_COMPONENT;
import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectKeyHelper.*;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentsView extends VerticalLayout implements BeforeEnterObserver {

    public static final String QUERY_PARAM_NAME_FILTER = "nameFilter";
    public static final String QUERY_PARAM_DESCRIPTION_FILTER = "descriptionFilter";

    private final transient ComponentService componentService;

    private transient ProjectKey projectKey;

    private final ComponentFilter componentFilter = new ComponentFilter();

    private QueryParamsHelper queryParamsHelper;

    private Binder<ComponentFilter> filterBinder = new Binder<>(ComponentFilter.class);

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.projectKey = getProjectKey(event.getRouteParameters());
        this.queryParamsHelper = new QueryParamsHelper(event.getLocation()).withParameter(QUERY_PARAM_NAME_FILTER, nameFilter -> {
            componentFilter.setNameFilter(nameFilter);
        }).withParameter(QUERY_PARAM_DESCRIPTION_FILTER, descriptionFilter -> {
            componentFilter.setDescriptionFilter(descriptionFilter);
        });
        filterBinder.readBean(componentFilter);
    }

    @PostConstruct
    void init() {
        setSizeFull();

        FilterableGrid<ComponentSummary, ComponentFilter> filterableGrid = FilterableGrid.builder(ComponentSummary.class,
            new CallbackDataProvider<ComponentSummary, ComponentFilter>(query -> stream(
                componentService.findAll(projectKey, query.getFilter(), query.getOffset(), query.getLimit())
                    .spliterator(), false), query -> componentService.countAll(projectKey, query.getFilter())), componentFilter);

        // Name
        TextField nameFilterTextBox = filterableGrid.text("Name", ComponentFilter::setNameFilter);
        filterableGrid.withColumn(nameFilterTextBox, componentSummary -> {
            Span span = new Span(componentSummary.getLatestVersion()
                .getName());
            span.setTitle(componentSummary.getComponent().getName());
            return span;
        });
        // Description
        TextField descriptionFilterTextBox = filterableGrid.text("Description", ComponentFilter::setDescriptionFilter);
        filterableGrid.withColumn(descriptionFilterTextBox, componentSummary -> {
            String description = componentSummary.getLatestVersion().getDescription();
            Span span = new Span(abbreviate(description, "...", 256));
            if (description != null) {
                span.setTitle(description);
            }
            return span;
        });

        // URL
        filterableGrid.withColumn("Homepage", componentSummary -> new Span(componentSummary.getLatestVersion()
            .getUrl()));
        // Latest Version
        filterableGrid.withColumn("Latest Version", componentSummary -> new Span(componentSummary.getLatestVersion()
            .getVersion()));
        // Latest Version
        filterableGrid.withColumn("Updated", componentSummary -> new Span(componentSummary.getLatestVersion()
            .getUpdatedAt()
            .format(ISO_LOCAL_DATE_TIME)));
        // Version #
        filterableGrid.withColumn("Available Versions", componentSummary -> new Span(Long.toString(componentSummary.getVersionCount())));

        filterBinder.forField(nameFilterTextBox)
            .bind(filter -> filter.getNameFilter(), ComponentFilter::setNameFilter);
        filterBinder.forField(descriptionFilterTextBox)
            .bind(filter -> filter.getDescriptionFilter(), ComponentFilter::setNameFilter);
        filterableGrid.addFilterListener(filter -> {
            queryParamsHelper.update(getUI(), uriBuilder -> {
                if (isNotBlank(componentFilter.getNameFilter())) {
                    uriBuilder.queryParam(QUERY_PARAM_NAME_FILTER, componentFilter.getNameFilter());
                }
                if (isNotBlank(componentFilter.getDescriptionFilter())) {
                    uriBuilder.queryParam(QUERY_PARAM_DESCRIPTION_FILTER, componentFilter.getDescriptionFilter());
                }
            });
        });

        Grid<ComponentSummary> grid = filterableGrid.build();

        grid.addItemClickListener(event -> UI.getCurrent()
            .navigate(ComponentView.class, new RouteParam(PARAMETER_OWNER, this.projectKey.getOwner()),
                new RouteParam(PARAMETER_PROJECT, this.projectKey.getProject()), new RouteParam(PARAMETER_COMPONENT, event.getItem()
                    .getComponent()
                    .getName())));
        this.add(grid);
    }

}
