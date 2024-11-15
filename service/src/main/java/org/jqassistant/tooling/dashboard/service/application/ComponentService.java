package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    public Component resolve(String ownerId, String projectId, String componentId) {
        return componentRepository.resolve(projectId, componentId);
    }
}
