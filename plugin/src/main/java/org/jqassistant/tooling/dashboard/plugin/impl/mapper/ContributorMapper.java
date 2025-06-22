
package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Component;
import org.jqassistant.tooling.dashboard.plugin.api.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface ContributorMapper {

    ContributorMapper MAPPER = Mappers.getMapper(ContributorMapper.class);

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate", "component" })
    ContributorDTO toDTO(Contributor contributor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delegate", ignore = true)
    @Mapping(target = "component", source = "component")
    Contributor contributedto(ProjectKey projectKey, Component component, ContributorDTO dto);

    ContributorDTO toDTO(String contributor);
}

// was ist beanmapping ?
