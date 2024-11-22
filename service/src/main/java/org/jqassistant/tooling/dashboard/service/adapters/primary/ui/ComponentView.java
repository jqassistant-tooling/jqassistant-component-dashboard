package org.jqassistant.tooling.dashboard.service.adapters.primary.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.springframework.transaction.annotation.Transactional;

@RoutePrefix("ui")
@Route(value = ":owner/:project/components/:componentId", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class ComponentView extends VerticalLayout {

    private final transient ComponentService componentService;

    @PostConstruct
    void init() {

    }

}
