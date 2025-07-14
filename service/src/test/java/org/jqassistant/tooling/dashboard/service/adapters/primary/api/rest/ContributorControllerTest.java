package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.ContributorMapper;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.ContributorService;
import org.jqassistant.tooling.dashboard.service.application.EntitySupplier;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jqassistant.tooling.dashboard.service.ModelFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContributorController.class)
class ContributorControllerTest {

    @MockitoBean
    private ContributorService contributorService;

    @MockitoBean
    private ContributorMapper contributorMapper;

    @Captor
    private ArgumentCaptor<ProjectKey> projectKeyArgumentCaptor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void setContributors() throws Exception {
        Contributor contributor = stubContributor("MaxMustermann");
        doReturn(contributor).when(contributorMapper)
            .toContributor(any(ContributorDTO.class));

        mockMvc.perform(put("/api/rest/v1/jqassistant/plugins/test/contributors").content("""
                    [
                      { "ident": "MaxMustermann" }
                    ]"
                    """)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("user").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(contributorService).setContributors(projectKeyArgumentCaptor.capture(), eq("test"), eq(List.of(contributor)));
        ProjectKey projectKey = projectKeyArgumentCaptor.getValue();
        assertThat(projectKey.getOwner()).isEqualTo("jqassistant");
        assertThat(projectKey.getProject()).isEqualTo("plugins");
        verify(contributorMapper).toContributor(any(ContributorDTO.class));
    }

}
