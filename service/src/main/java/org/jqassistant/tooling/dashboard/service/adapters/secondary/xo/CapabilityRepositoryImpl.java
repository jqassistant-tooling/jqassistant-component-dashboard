package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityKey;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public class CapabilityRepositoryImpl extends AbstractXORepository<XOCapabilityRepository> implements CapabilityRepository {

    CapabilityRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOCapabilityRepository.class);
    }

    @Override
    public Capability find(Project project, CapabilityKey capabilityKey) {
        return getXORepository().find(project, capabilityKey.getType(), capabilityKey.getValue());
    }

    @Override
    public Capability resolve(Project project, String type, String value) {
        return getXORepository().resolve(project, type, value);
    }

    @Override
    public Stream<CapabilitySummary> findAll(Project project, Optional<CapabilityFilter> filter, int offset, int limit) {
        return getXORepository().findAll(project, filter.map(CapabilityFilter::getTypeFilter)
            .orElse(null), filter.map(CapabilityFilter::getValueFilter)
            .orElse(null), offset, limit);
    }

    @Override
    public int countAll(Project project, Set<String> typeFilter, List<String> valueFilter) {
        return getXORepository().countAll(project, typeFilter, valueFilter);
    }

    @Override
    public List<String> getTypes(Project project) {
        return getXORepository().getTypes(project);
    }

    @Override
    public List<Dependencies> getRequiredBy(Project project, CapabilityKey capabilityKey) {
        return getXORepository().getRequiredBy(project, capabilityKey.getType(), capabilityKey.getValue());
    }

    @Override
    public List<Dependencies> getProvidedBy(Project project, CapabilityKey capabilityKey) {
        return getXORepository().getProvidedBy(project, capabilityKey.getType(), capabilityKey.getValue());
    }
}
