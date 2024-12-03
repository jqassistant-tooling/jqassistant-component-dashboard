package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

import static org.jqassistant.tooling.dashboard.service.adapters.primary.ui.views.projects.ProjectView.getProjectKey;

@RoutePrefix("ui")
@Route(value = ":owner/:project/capabilities/:capabilityType/:capabilityValue", layout = DashboardLayout.class)
@AnonymousAllowed
@RequiredArgsConstructor
@Transactional
public class CapabilityView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PARAMETER_CAPABILITY_TYPE = "capabilityType";

    public static final String PARAMETER_CAPABILITY_VALUE = "capabilityValue";

    private final CapabilityService capabilityService;

    private final TransactionTemplate transactionTemplate;

    private H2 title = new H2();

    private Span description = new Span();

    private TreeGrid<TreeNode> providedBy = new TreeGrid<>();

    private TreeGrid<TreeNode> requiredBy = new TreeGrid<>();

    public static CapabilityKey getCapabilityKey(BeforeEnterEvent beforeEnterEvent) {
        ProjectKey projectKey = getProjectKey(beforeEnterEvent);
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

            Map<String, Object> versions = dependencies.getVersions();
            TreeNode.TreeNodeBuilder<Version> versionNodeBuilder = TreeNode.builder();
            Version version = (Version) versions.get("version");
            versionNodeBuilder.value(version);
            versionNodeBuilder.label(version.getVersion());

            List<File> files = (List<File>) versions.get("files");
            for (File file : files) {
                TreeNode.TreeNodeBuilder<File> fileNodeBuilder = TreeNode.builder();
                fileNodeBuilder.value(file);
                fileNodeBuilder.label(file.getFileName());
                TreeNode<File> fileTreeNode = fileNodeBuilder.build();
                versionNodeBuilder.child(fileTreeNode);
            }
            TreeNode<Version> versionTreeNode = versionNodeBuilder.build();
            componentNodeBuilder.child(versionTreeNode);
            treeItems.add(componentNodeBuilder.build());
        }
        return treeItems;
    }

}