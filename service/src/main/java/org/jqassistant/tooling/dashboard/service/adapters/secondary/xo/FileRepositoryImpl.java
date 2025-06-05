package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import org.jqassistant.tooling.dashboard.service.application.FileRepository;
import org.jqassistant.tooling.dashboard.service.application.model.File;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepositoryImpl extends AbstractXORepository<XOFileRepository> implements FileRepository {

    FileRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOFileRepository.class);
    }

    @Override
    public File create() {
        return getXORepository().create();
    }
}
