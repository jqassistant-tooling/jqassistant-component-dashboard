package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Version;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface VersionMapper {

    VersionMapper MAPPER = Mappers.getMapper(VersionMapper.class);

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate", "component" })
    VersionDTO toDTO(Version version);

}
