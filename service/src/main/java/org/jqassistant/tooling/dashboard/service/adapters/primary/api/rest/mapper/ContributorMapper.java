package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = FileMapper.class)
public abstract class ContributorMapper {

    @Autowired
    private ContributorService contributorService;

    public abstract Contributor contributedto(@Context Project project, @Context Component component, ContributorDTO dto);

    @ObjectFactory
    Version resolve(@Context Component component, ContributorDTO contributorDTO) {
        return contributorService.resolve(component, contributorDTO.getContributor());
    }

}
