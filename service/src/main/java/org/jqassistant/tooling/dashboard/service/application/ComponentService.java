package org.jqassistant.tooling.dashboard.service.application;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    public Component resolve(String ownerId, String projectId, String componentId) {
        return componentRepository.resolve(projectId, componentId);
    }

    public Iterable<Component> findAll(Optional<ComponentFilter> filter, int offset, int limit) {
        return componentRepository.findAll(filter.map(ComponentFilter::getNameFilter)
            .orElse(null), offset, limit);
    }

    public int countAll(Optional<ComponentFilter> filter) {
        return componentRepository.countAll(filter.map(ComponentFilter::getNameFilter)
            .orElse(null));
    }
}
