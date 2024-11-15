package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.CapabilityDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Capability;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface CapabilityMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate" })
    CapabilityDTO toDTO(Capability capability);

}
