package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.VersionRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Repository;

@Repository
public class VersionRepositoryImpl extends AbstractXORepository<XOVersionRepository> implements VersionRepository {

    VersionRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOVersionRepository.class);
    }

    @Override
    public void remove(Component component, String version) {
        getXORepository().remove(component, version);
    }

    @Override
    public Version create(Component component, String version) {
        return getXORepository().create(component, version);
    }
}
