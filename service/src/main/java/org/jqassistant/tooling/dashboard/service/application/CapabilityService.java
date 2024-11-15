package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CapabilityService {

    private final CapabilityRepository capabilityRepository;

    public Capability resolve(String type, String value) {
        return capabilityRepository.resolve(type, value);
    }
}
