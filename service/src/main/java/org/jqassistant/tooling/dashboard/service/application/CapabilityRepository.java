package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository {

    List<String> getTypes(Project project);

    Capability find(Project project, CapabilityKey capabilityKey);

    Capability resolve(Project project, String type, String value);

    Stream<CapabilitySummary> findAll(Project project, Optional<CapabilityFilter> filter, int offset, int limit);

    interface CapabilitySummary {

        Capability getCapability();

        List<Component> getProvidedByComponents();

    }

    int countAll(Project project, Set<String> typeFilter, List<String> valueFilter);

    List<Dependencies> getRequiredBy(Project project, CapabilityKey capabilityKey);

    List<Dependencies> getProvidedBy(Project project, CapabilityKey capabilityKey);

    interface Dependencies {

        Component getComponent();

        List<VersionDependency> getVersions();

        interface VersionDependency {

            Version getVersion();

            List<File> getFiles();

        }

    }
}
