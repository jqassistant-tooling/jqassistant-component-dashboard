package org.jqassistant.tooling.dashboard.service.application.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CapabilityFilter {

    private Set<String> typeFilter;

    private String valueFilter;

}
