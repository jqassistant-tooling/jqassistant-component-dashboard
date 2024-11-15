package org.jqassistant.tooling.dashboard.service.application;

import com.buschmais.xo.api.annotation.Repository;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;

@Repository
public interface VersionRepository {

    void remove(Component component, String version);

    Version create(Component component, String version);

}
