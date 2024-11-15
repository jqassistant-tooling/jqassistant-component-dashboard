package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.File;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

    File create();

}
