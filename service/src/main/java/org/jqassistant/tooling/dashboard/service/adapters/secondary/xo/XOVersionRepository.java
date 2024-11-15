package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import lombok.RequiredArgsConstructor;
import org.jqassistant.tooling.dashboard.service.application.VersionRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class XOVersionRepository implements VersionRepository {

    private final XOManager xoManager;

    @Override
    public Version resolve(String ownerId, String projectId, String componentId, String versionId) {
        return xoManager.create(Version.class);
    }
}
