package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper;

import org.jqassistant.tooling.dashboard.api.dto.FileDTO;
import org.jqassistant.tooling.dashboard.service.application.FileService;
import org.jqassistant.tooling.dashboard.service.application.model.File;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = CapabilityMapper.class)
public abstract class FileMapper {

    @Autowired
    private FileService fileService;

    abstract File toFile(@Context Project project, FileDTO fileDTO);

    @ObjectFactory
    File create() {
        return this.fileService.create();
    }
}
