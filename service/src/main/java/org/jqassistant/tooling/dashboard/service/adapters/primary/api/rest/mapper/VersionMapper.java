package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public interface VersionMapper extends ModelMapper<Version, VersionDTO> {

    @BeanMapping(ignoreUnmappedSourceProperties = { "containsFiles" })
    Version toVersion(VersionDTO dto, @Context VersionService versionService);

    @ObjectFactory
    default Version resolve(@Context VersionService versionService) {
        return versionService.resolve(null, null, null, null);
    }

}
