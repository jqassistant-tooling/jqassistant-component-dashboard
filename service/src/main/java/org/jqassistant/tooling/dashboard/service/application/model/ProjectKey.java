package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectKey {

    private final String owner;

    private final String project;


}
