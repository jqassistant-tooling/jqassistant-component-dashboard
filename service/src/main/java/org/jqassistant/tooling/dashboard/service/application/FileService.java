package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.model.File;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public File create() {
        return fileRepository.create();
    }
}
