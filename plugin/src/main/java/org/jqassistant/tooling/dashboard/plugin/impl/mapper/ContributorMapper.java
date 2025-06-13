
package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Contributor;
import org.jqassistant.tooling.dashboard.plugin.api.model.Version;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface ContributorMapper {

    ContributorMapper MAPPER = Mappers.getMapper(ContributorMapper.class);

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate", "component" })
    ContributorDTO toDTO(org.jqassistant.tooling.dashboard.plugin.impl.mapper.Contributor contributor);

}
