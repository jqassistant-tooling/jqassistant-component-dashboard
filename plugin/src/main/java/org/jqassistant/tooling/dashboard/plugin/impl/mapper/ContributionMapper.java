package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface ContributionMapper {

    ContributionMapper MAPPER = Mappers.getMapper(ContributionMapper.class);


    //ContributionDTO toDTO();
}
