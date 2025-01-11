package org.jqassistant.tooling.dashboard.service.application;

import java.time.ZonedDateTime;

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
import static org.mockito.Mockito.*;

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

        createOrUpdate(project, component, version);

        verify(component).setLatestVersion(version);
    }

    @Test
    void updateLatestVersion() {
        Project project = stubProject("plugins");
        Component component = stubComponent("test");
        Version existingVersion = stubVersion("1.0.0");
        ZonedDateTime now = ZonedDateTime.now();
        doReturn(now.minusHours(1)).when(existingVersion)
            .getUpdatedAt();
        doReturn(existingVersion).when(component)
            .getLatestVersion();
        Version updatedVersion = stubVersion("1.1.0");
        doReturn(now).when(updatedVersion)
            .getUpdatedAt();

        createOrUpdate(project, component, updatedVersion);

        verify(component).setLatestVersion(updatedVersion);
    }

    @Test
    void keepLatestVersion() {
        Project project = stubProject("plugins");
        Component component = stubComponent("test");
        Version existingVersion = stubVersion("1.0.0");
        ZonedDateTime now = ZonedDateTime.now();
        doReturn(now).when(existingVersion)
            .getUpdatedAt();
        doReturn(existingVersion).when(component)
            .getLatestVersion();
        Version updatedVersion = stubVersion("1.1.0");
        doReturn(now.minusHours(1)).when(updatedVersion)
            .getUpdatedAt();

        createOrUpdate(project, component, updatedVersion);

        verify(component, never()).setLatestVersion(updatedVersion);
    }

    private void createOrUpdate(Project project, Component component, Version updatedVersion) {
        ProjectKey projectKey = new ProjectKey("jqassistant", "plugins");
        doReturn(project).when(projectService)
            .find(projectKey);
        doReturn(component).when(componentService)
            .resolve(projectKey, "test");
        doReturn(updatedVersion).when(versionSupplier)
            .get(project, component);

        versionService.createOrUpdate(projectKey, "test", "1.0.0", versionSupplier);

        verify(projectService).find(projectKey);
        verify(componentService).resolve(projectKey, "test");
        verify(versionRepository).remove(component, "1.0.0");
    }
}
