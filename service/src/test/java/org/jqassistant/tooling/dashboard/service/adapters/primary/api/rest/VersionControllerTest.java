package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.ProjectService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ComponentService componentService;

    @MockBean
    private VersionService versionService;

    @MockBean
    private VersionMapper versionMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void updateVersion() throws Exception {
        Project project = project("plugins");
        Component component = component("test");

        ProjectKey projectKey = new ProjectKey("jqassistant", "plugins");
        doReturn(project).when(projectService)
            .find(projectKey);
        doReturn(component).when(componentService)
            .resolve(projectKey, "test");
        Version version = mock(Version.class);
        doReturn(version).when(versionMapper)
            .toVersion(eq(project), eq(component), any(VersionDTO.class));

        mockMvc.perform(put("/api/rest/v1/jqassistant/plugins/test/1.0.0").content("{ }")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("user").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(projectService).find(projectKey);
        verify(componentService).resolve(projectKey, "test");
        verify(versionService).remove(component, "1.0.0");
        verify(versionMapper).toVersion(eq(project), eq(component), any(VersionDTO.class));
    }

    private static Component component(String name) {
        Component component = mock(Component.class);
        doReturn(name).when(component)
            .getName();
        return component;
    }

    private static Project project(String name) {
        Project project = mock(Project.class);
        doReturn(name).when(project)
            .getName();
        return project;
    }

}
