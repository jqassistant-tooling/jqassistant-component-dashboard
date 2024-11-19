package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.buschmais.xo.api.Query;
import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
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
    public Stream<CapabilitySummary> findAll(Optional<CapabilityFilter> filter, int offset, int limit) {
        Query.Result<? extends CapabilityRepository.CapabilitySummary> result = xoManager.createQuery(XOCapabilityRepository.CapabilitySummary.class)
            .withParameter("typeFilter", filter.map(f -> f.getTypeFilter())
                .orElse(null))
            .withParameter("valueFilter", filter.map(f -> f.getValueFilter())
                .orElse(null))
            .withParameter("offset", offset)
            .withParameter("limit", limit)
            .execute();
        return toStream(result).map(summary -> summary);
    }

    @Override
    public int countAll(String typeFilter, String valueFilter) {
        return getXORepository().countAll(typeFilter, valueFilter);
    }

    @Override
    public List<String> getTypes() {
        Query.Result<XOCapabilityRepository.Types> result = xoManager.createQuery(XOCapabilityRepository.Types.class)
            .execute();
        return toStream(result).map(XOCapabilityRepository.Types::getType)
            .toList();
    }
}
