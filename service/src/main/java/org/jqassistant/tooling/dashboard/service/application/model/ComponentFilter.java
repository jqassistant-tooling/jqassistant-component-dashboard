package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@Setter
@ToString
public class ComponentFilter {

    private List<String> nameFilter = emptyList();

    private List<String> descriptionFilter = emptyList();

}
