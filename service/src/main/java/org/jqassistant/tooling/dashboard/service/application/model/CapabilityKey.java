package org.jqassistant.tooling.dashboard.service.application.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CapabilityKey {

    private final ProjectKey projectKey;

    private final String type;

    private final String value;

}
