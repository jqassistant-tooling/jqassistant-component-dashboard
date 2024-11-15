package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.FileDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.File;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(uses = CapabilityMapper.class)
public interface FileMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate", "parents" })
    FileDTO toDTO(File file);

}
