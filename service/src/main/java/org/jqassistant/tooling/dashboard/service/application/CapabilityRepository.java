package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;

import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository {

    Capability resolve(String type, String name);

    Iterable<Capability> findAll(String typeFilter, String valueFilter, int offset, int limit);

    int countAll(String typeFilter, String valueFilter);

    List<String> getTypes();

}
