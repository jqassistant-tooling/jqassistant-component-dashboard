package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ProjectKey {

    private final String owner;

    private final String project;
}
