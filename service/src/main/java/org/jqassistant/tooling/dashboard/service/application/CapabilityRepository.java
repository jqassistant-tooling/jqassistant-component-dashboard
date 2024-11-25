package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository {

    List<String> getTypes();

    Capability find(String type, String value);

    Capability resolve(String type, String value);

    Stream<CapabilitySummary> findAll(Optional<CapabilityFilter> filter, int offset, int limit);

    interface CapabilitySummary {

        Capability getCapability();

        List<Component> getProvidedByComponents();

    }

    int countAll(Set<String> typeFilter, String valueFilter);

    Stream<Dependencies> getRequiredBy(Capability capability);

    Stream<Dependencies> getProvidedBy(Capability capability);

    interface Dependencies {

        Component getComponent();

        Map<String, Object> getVersions();

    }
}
