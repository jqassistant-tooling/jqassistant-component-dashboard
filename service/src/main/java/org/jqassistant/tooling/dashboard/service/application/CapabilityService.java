package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.CapabilityFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityService {

    private final CapabilityRepository capabilityRepository;

    public Capability resolve(String type, String value) {
        return capabilityRepository.resolve(type, value);
    }

    public Stream<CapabilityRepository.CapabilitySummary> findAll(Optional<CapabilityFilter> filter, int offset, int limit) {
        return capabilityRepository.findAll(filter, offset, limit);
    }

    public int countAll(Optional<CapabilityFilter> filter) {
        return capabilityRepository.countAll(filter.map(CapabilityFilter::getTypeFilter)
            .orElse(null), filter.map(CapabilityFilter::getValueFilter)
            .orElse(null));
    }

    public List<String> getTypes() {
        return capabilityRepository.getTypes();
    }
}
