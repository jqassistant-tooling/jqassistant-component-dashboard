package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.stream.Stream;

import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Repository;

@Repository
public class ComponentRepositoryImpl extends AbstractXORepository<XOComponentRepository> implements ComponentRepository {
    protected ComponentRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOComponentRepository.class);
    }

    @Override
    public Component resolve(String projectId, String componentId) {
        return getXORepository().resolve(projectId, componentId);
    }

    @Override
    public Stream<Component> findAll(String nameFilter, int offset, int limit) {
        return toStream(getXORepository().findAll(nameFilter, offset, limit)).map(co -> co);
    }

    @Override
    public int countAll(String nameFilter) {
        return getXORepository().countAll(nameFilter);
    }
}
