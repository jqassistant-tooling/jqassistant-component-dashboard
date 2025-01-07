package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.jqassistant.tooling.dashboard.service.ModelFixture.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private ComponentService componentService;

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private EntitySupplier<Version> versionSupplier;

    private VersionService versionService;

    @BeforeEach
    void setUp() {
        versionService = new VersionService(projectService, componentService, versionRepository);
    }

    @Test
    void createVersion() {
        Project project = stubProject("plugins");
        Component component = stubComponent("test");
        Version version = stubVersion("1.0.0");

        ProjectKey projectKey = new ProjectKey("jqassistant", "plugins");
        doReturn(project).when(projectService)
            .find(projectKey);
        doReturn(component).when(componentService)
            .resolve(projectKey, "test");
        doReturn(version).when(versionSupplier)
            .get(project, component);

        versionService.createOrUpdate(projectKey, "test", "1.0.0", versionSupplier);

        verify(projectService).find(projectKey);
        verify(componentService).resolve(projectKey, "test");
        verify(versionRepository).remove(component, "1.0.0");
        verify(component).setLatestVersion(version);

    }

}
