package org.jqassistant.tooling.dashboard.service.application;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository.ComponentSummary;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.ComponentFilter;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentService {

    private final ProjectService projectService;

    private final ComponentRepository componentRepository;

    public Component resolve(ProjectKey projectKey, String component) {
        Project project = projectService.find(projectKey);
        return componentRepository.resolve(project.getName(), component);
    }

    public Stream<ComponentSummary> findAll(ProjectKey projectKey, Optional<ComponentFilter> filter, int offset, int limit) {
        Project project = projectService.find(projectKey);
        return componentRepository.findAll(project, filter.map(ComponentFilter::getNameFilter)
            .orElse(null), filter.map(ComponentFilter::getDescriptionFilter)
            .orElse(null), offset, limit);
    }

    public int countAll(ProjectKey projectKey, Optional<ComponentFilter> filter) {
        Project project = projectService.find(projectKey);
        return componentRepository.countAll(project, filter.map(ComponentFilter::getNameFilter)
            .orElse(null), filter.map(ComponentFilter::getDescriptionFilter)
            .orElse(null));
    }
}
