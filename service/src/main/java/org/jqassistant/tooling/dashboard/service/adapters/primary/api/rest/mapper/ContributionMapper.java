package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;


import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.jqassistant.tooling.dashboard.service.application.ContributionService;
import org.jqassistant.tooling.dashboard.service.application.model.Contribution;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = ContributorMapper.class)
public abstract class ContributionMapper {


    @Autowired
    private ContributionService contributionService;

    @Mapping(target = "toComponent", ignore = true)
    public abstract Contribution toContribution(ContributionDTO dto, @Context ProjectKey projectKey, @Context String componentID);

    @ObjectFactory
    Contribution resolve(ContributionDTO contributionDTO, @Context ProjectKey projectKey, @Context String componentID){
        return contributionService.resolve(projectKey, componentID, contributionDTO.getContributor().getIdent());
    }
}
