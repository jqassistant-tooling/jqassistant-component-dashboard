
package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Contributor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface ContributorMapper {

    ContributorMapper MAPPER = Mappers.getMapper(ContributorMapper.class);

    @BeanMapping(ignoreUnmappedSourceProperties = { "contributedComponents" })
    @Mapping(target = "ident" , ignore = true)
    @Mapping(target = "email" , ignore = true)
    ContributorDTO toDTO(Contributor contributor);
}

// was ist beanmapping ?
