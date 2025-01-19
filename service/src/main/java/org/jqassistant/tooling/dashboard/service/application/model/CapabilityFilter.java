package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

@Getter
@Setter
@ToString
public class CapabilityFilter {

    private Set<String> typeFilter = emptySet();

    private List<String> valueFilter = emptyList();

}
