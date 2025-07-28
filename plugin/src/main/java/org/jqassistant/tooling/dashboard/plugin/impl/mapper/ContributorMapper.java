
package org.jqassistant.tooling.dashboard.plugin.impl.mapper;

import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = FileMapper.class)
public interface ContributorMapper {

    ContributorMapper MAPPER = Mappers.getMapper(ContributorMapper.class);

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "delegate", "commits" })
    @Mapping(target = "ident", source = "identString")
    ContributorDTO toDTO(GitAuthorDescriptor contributor);
}
