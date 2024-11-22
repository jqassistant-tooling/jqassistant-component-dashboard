package org.jqassistant.tooling.dashboard.service.adapters.primary.ui;

import java.util.List;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities/:capabilityType/:capabilityValue", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilityView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PARAMETER_CAPABILITY_TYPE = "capabilityType";
    public static final String PARAMETER_CAPABILITY_VALUE = "capabilityValue";

    private final transient CapabilityService capabilityService;

    private final TransactionTemplate transactionTemplate;

    private H2 title = new H2();

    private Span description = new Span();

    private Span requiredBy = new Span();

    @PostConstruct
    void init() {
        add(title);
        add(description);
        add(new H3("Required By"));
        add(requiredBy);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            String capabilityType = event.getRouteParameters()
                .get(PARAMETER_CAPABILITY_TYPE)
                .orElse(null);
            String capabilityValue = event.getRouteParameters()
                .get(CapabilityView.PARAMETER_CAPABILITY_VALUE)
                .orElse(null);
            Capability capability = capabilityService.find(capabilityType, capabilityValue);
            this.title.setText(String.format("%s '%s'", capabilityType, capabilityValue));
            this.description.setText(capability.getDescription());
            requiredBy.setText(capabilityService.getRequiredByBy(capability)
                .map(r -> r.getComponent()
                    .getName())
                .collect(joining(", ")));

        });
    }

    @Getter
    @Builder
    @ToString
    private static class TreeNode<V> {

        private V value;

        private String label;

        @Builder.Default
        private List<TreeNode> children = emptyList();
    }

}
