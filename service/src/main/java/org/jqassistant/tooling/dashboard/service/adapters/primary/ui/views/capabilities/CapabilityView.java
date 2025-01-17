package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.DashboardLayout;
import org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared.TreeNode;
import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectKeyHelper.getProjectKey;

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

    private final H2 title = new H2();

    private final Span description = new Span();

    private final TreeGrid<TreeNode> providedBy = new TreeGrid<>();

    private final TreeGrid<TreeNode> requiredBy = new TreeGrid<>();

    private static CapabilityKey getCapabilityKey(BeforeEnterEvent beforeEnterEvent) {
        ProjectKey projectKey = getProjectKey(beforeEnterEvent.getRouteParameters());
        RouteParameters routeParameters = beforeEnterEvent.getRouteParameters();
        String capabilityType = routeParameters.get(PARAMETER_CAPABILITY_TYPE)
            .orElseThrow();
        String capabilityValue = routeParameters.get(PARAMETER_CAPABILITY_VALUE)
            .orElseThrow();
        return new CapabilityKey(projectKey, capabilityType, capabilityValue);
    }

    @PostConstruct
    void init() {
        add(title);
        add(description);
        add(new H3("Provided By"));
        providedBy.addHierarchyColumn(TreeNode::getLabel)
            .setHeader("Component");
        add(providedBy);
        add(new H3("Required By"));
        requiredBy.addHierarchyColumn(TreeNode::getLabel)
            .setHeader("Component");
        add(requiredBy);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        transactionTemplate.executeWithoutResult(tx -> {
            CapabilityKey capabilityKey = getCapabilityKey(event);
            Capability capability = capabilityService.find(capabilityKey);
            this.title.setText(String.format("%s '%s'", capability.getType(), capability.getValue()));
            this.description.setText(capability.getDescription());
            this.providedBy.setItems(getTreeNodes(capabilityService.getProvidedBy(capabilityKey)), TreeNode::getChildren);
            this.requiredBy.setItems(getTreeNodes(capabilityService.getRequiredByBy(capabilityKey)), TreeNode::getChildren);
        });
    }

    private static List<TreeNode> getTreeNodes(Stream<CapabilityRepository.Dependencies> requiredBy) {
        List<TreeNode> treeItems = new ArrayList<>();
        for (CapabilityRepository.Dependencies dependencies : requiredBy.toList()) {
            TreeNode.TreeNodeBuilder<Component> componentNodeBuilder = TreeNode.builder();

            Component component = dependencies.getComponent();
            componentNodeBuilder.value(component);
            componentNodeBuilder.label(component.getName());

            List<Map<String, Object>> versions = dependencies.getVersions();
            for (Map<String, Object> versionEntry : versions) {
                TreeNode.TreeNodeBuilder<Version> versionNodeBuilder = TreeNode.builder();
                Version version = (Version) versionEntry.get("version");
                versionNodeBuilder.value(version);
                versionNodeBuilder.label(version.getVersion());

                List<File> files = (List<File>) versionEntry.get("files");
                for (File file : files) {
                    TreeNode.TreeNodeBuilder<File> fileNodeBuilder = TreeNode.builder();
                    fileNodeBuilder.value(file);
                    fileNodeBuilder.label(file.getFileName());
                    versionNodeBuilder.child(fileNodeBuilder.build());
                }

                componentNodeBuilder.child(versionNodeBuilder.build());
            }
            treeItems.add(componentNodeBuilder.build());
        }
        return treeItems;
    }

}
