package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository {

    Capability resolve(String type, String name);

    Stream<CapabilitySummary> findAll(Optional<CapabilityFilter> filter, int offset, int limit);

    int countAll(Set<String> typeFilter, String valueFilter);

    List<String> getTypes();

    interface CapabilitySummary {

        Capability getCapability();

        List<Component> getProvidedByComponents();

    }

}
