package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = FileMapper.class)
public abstract class ContributorMapper {

    @Autowired
    private ContributorService contributorService;

    @Mapping(target = "identString", ignore = true)
    @Mapping(target = "contributedTo", ignore = true)
    @BeanMapping(ignoreUnmappedSourceProperties = "ident")
    public abstract Contributor toContributor(ContributorDTO dto);

    @ObjectFactory
    Contributor resolve(ContributorDTO contributorDTO) {
        return contributorService.resolve(contributorDTO.getIdent());
    }


}
