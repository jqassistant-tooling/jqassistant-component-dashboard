package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.CapabilityDTO;
import org.jqassistant.tooling.dashboard.service.application.CapabilityService;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CapabilityMapper {

    @Autowired
    private CapabilityService capabilityService;

    abstract Capability toCapability(@Context Project project, CapabilityDTO capabilityDTO);

    @ObjectFactory
    Capability create(@Context Project project, CapabilityDTO capabilityDTO) {
        return this.capabilityService.resolve(project, capabilityDTO.getType(), capabilityDTO.getValue());
    }
}
