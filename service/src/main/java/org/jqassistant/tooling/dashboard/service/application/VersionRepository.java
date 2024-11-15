package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Version;

public interface VersionRepository {

    Version resolve(String ownerId, String projectId, String componentId, String versionId);

}
