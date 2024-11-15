package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = FileMapper.class)
public abstract class VersionMapper {

    @Autowired
    private VersionService versionService;

    public abstract Version toVersion(@Context Component component, VersionDTO dto);

    @ObjectFactory
    Version resolve(VersionDTO versionDTO, @Context Component component) {
        return versionService.resolve(component, versionDTO.getVersion());
    }

}
