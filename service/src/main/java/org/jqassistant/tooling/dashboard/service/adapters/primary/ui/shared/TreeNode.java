package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TreeNode<V> {

    private V value;

    private String label;

    @Singular
    private List<TreeNode> children;
}
