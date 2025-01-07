package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.EntitySupplier;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jqassistant.tooling.dashboard.service.ModelFixture.*;
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
    private VersionService versionService;

    @MockBean
    private VersionMapper versionMapper;

    @Captor
    private ArgumentCaptor<ProjectKey> projectKeyArgumentCaptor;

    @Captor
    private ArgumentCaptor<EntitySupplier<Version>> entitySupplierArgumentCaptor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void updateVersion() throws Exception {
        Project project = stubProject("plugins");
        Component component = stubComponent("test");
        Version version = stubVersion("1.0.0");
        doAnswer(invocation -> {
            EntitySupplier<Version> versionSupplier = invocation.getArgument(3);
            return versionSupplier.get(project, component);
        }).when(versionService)
            .createOrUpdate(any(ProjectKey.class), eq("test"), eq("1.0.0"), any(EntitySupplier.class));
        doReturn(version).when(versionMapper)
            .toVersion(eq(project), eq(component), any(VersionDTO.class));

        mockMvc.perform(put("/api/rest/v1/jqassistant/plugins/test/1.0.0").content("{ \"version\": \"1.0.0\" }")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("user").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(versionService).createOrUpdate(projectKeyArgumentCaptor.capture(), eq("test"), eq("1.0.0"), entitySupplierArgumentCaptor.capture());
        ProjectKey projectKey = projectKeyArgumentCaptor.getValue();
        assertThat(projectKey.getOwner()).isEqualTo("jqassistant");
        assertThat(projectKey.getProject()).isEqualTo("plugins");
        verify(versionMapper).toVersion(eq(project), eq(component), any(VersionDTO.class));
    }

}
