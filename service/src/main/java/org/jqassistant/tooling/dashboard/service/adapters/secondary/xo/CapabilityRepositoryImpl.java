package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.springframework.stereotype.Repository;

@Repository
public class CapabilityRepositoryImpl extends AbstractXORepository<XOCapabilityRepository> implements CapabilityRepository {

    CapabilityRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOCapabilityRepository.class);
    }

    @Override
    public Capability resolve(String type, String name) {
        return getXORepository().resolve(type, name);
    }

    @Override
    public ResultIterable<Capability> findAll(String typeFilter, String valueFilter, int offset, int limit) {
        return getXORepository().findAll(typeFilter, valueFilter, offset, limit);
    }

    @Override
    public int countAll(String typeFilter, String valueFilter) {
        return getXORepository().countAll(typeFilter, valueFilter);
    }
}