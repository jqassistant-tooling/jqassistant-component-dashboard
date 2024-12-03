package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ComponentKey {

    private final ProjectKey projectKey;

    private final String name;

}
