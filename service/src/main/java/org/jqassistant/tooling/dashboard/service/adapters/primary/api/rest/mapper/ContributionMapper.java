package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;


import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.jqassistant.tooling.dashboard.service.application.ContributionService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = ContributorMapper.class)
public abstract class ContributionMapper {


    @Autowired
    private ContributionService contributionService;

    @Mapping(target = "toComponent", ignore = true)
    public abstract Contributions toContribution(ContributionDTO dto, @Context String componentID);

    @ObjectFactory
    Contributions resolve(ContributionDTO contributionDTO, @Context String componentID){
        return contributionService.resolve(contributionDTO.getContributor().getIdent(), componentID);
    }
}
