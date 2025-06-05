package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository {

    void remove(Component component, String version);

    Version create(Component component, String version);

}
