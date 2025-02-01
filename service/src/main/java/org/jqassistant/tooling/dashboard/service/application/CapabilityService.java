package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityService {

    private final ProjectService projectService;

    private final CapabilityRepository capabilityRepository;

    public Capability resolve(Project project, String type, String value) {
        return capabilityRepository.resolve(project, type, value);
    }

    public Capability find(CapabilityKey capabilityKey) {
        Project project = projectService.find(capabilityKey.getProjectKey());
        return capabilityRepository.find(project, capabilityKey);
    }

    public Stream<CapabilityRepository.CapabilitySummary> findAll(ProjectKey projectKey, Optional<CapabilityFilter> filter, int offset, int limit) {
        Project project = projectService.find(projectKey);
        return capabilityRepository.findAll(project, filter, offset, limit);
    }

    public int countAll(ProjectKey projectKey, Optional<CapabilityFilter> filter) {
        Project project = projectService.find(projectKey);
        return capabilityRepository.countAll(project, filter.map(CapabilityFilter::getTypeFilter)
            .orElse(null), filter.map(CapabilityFilter::getValueFilter)
            .orElse(null));
    }

    public List<String> getTypes(ProjectKey projectKey) {
        Project project = projectService.find(projectKey);
        return capabilityRepository.getTypes(project);
    }

    public List<CapabilityRepository.Dependencies> getRequiredByBy(CapabilityKey capabilityKey) {
        Project project = projectService.find(capabilityKey.getProjectKey());
        return capabilityRepository.getRequiredBy(project, capabilityKey);
    }

    public List<CapabilityRepository.Dependencies> getProvidedBy(CapabilityKey capabilityKey) {
        Project project = projectService.find(capabilityKey.getProjectKey());
        return capabilityRepository.getProvidedBy(project, capabilityKey);
    }
}
