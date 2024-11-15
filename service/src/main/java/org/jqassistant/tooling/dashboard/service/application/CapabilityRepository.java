package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository {

    Capability resolve(String type, String name);

}
