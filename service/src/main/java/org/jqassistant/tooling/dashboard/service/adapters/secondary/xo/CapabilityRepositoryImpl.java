package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.buschmais.xo.api.Query;
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
        return toStream(xoManager.createQuery(XOCapabilityRepository.XOCapabilitySummary.class)
            .withParameter("project", project)
            .withParameter("typeFilter", filter.map(CapabilityFilter::getTypeFilter)
                .orElse(null))
            .withParameter("valueFilter", filter.map(CapabilityFilter::getValueFilter)
                .orElse(null))
            .withParameter("offset", offset)
            .withParameter("limit", limit)
            .execute()).map(capabilitySummary -> capabilitySummary);
    }

    @Override
    public int countAll(Project project, Set<String> typeFilter, List<String> valueFilter) {
        return getXORepository().countAll(project, typeFilter, valueFilter);
    }

    @Override
    public List<String> getTypes(Project project) {
        Query.Result<XOCapabilityRepository.Types> result = xoManager.createQuery(XOCapabilityRepository.Types.class)
            .withParameter("project", project)
            .execute();
        return toStream(result).map(XOCapabilityRepository.Types::getType)
            .toList();
    }

    @Override
    public Stream<Dependencies> getRequiredBy(Project project, CapabilityKey capabilityKey) {
        Query.Result<XOCapabilityRepository.CapabilityRequiredBy> result = xoManager.createQuery(XOCapabilityRepository.CapabilityRequiredBy.class)
            .withParameter("project", project)
            .withParameter("type", capabilityKey.getType())
            .withParameter("value", capabilityKey.getValue())
            .execute();
        return toStream(result).map(capabilityRequiredBy -> capabilityRequiredBy);
    }

    @Override
    public Stream<Dependencies> getProvidedBy(Project project, CapabilityKey capabilityKey) {
        Query.Result<XOCapabilityRepository.CapabilityProvidedBy> result = xoManager.createQuery(XOCapabilityRepository.CapabilityProvidedBy.class)
            .withParameter("project", project)
            .withParameter("type", capabilityKey.getType())
            .withParameter("value", capabilityKey.getValue())
            .execute();
        return toStream(result).map(capabilityRequiredBy -> capabilityRequiredBy);
    }
}
