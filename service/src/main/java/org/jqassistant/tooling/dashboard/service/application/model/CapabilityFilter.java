package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class CapabilityFilter {

    private Set<String> typeFilter;

    private String valueFilter;

}
