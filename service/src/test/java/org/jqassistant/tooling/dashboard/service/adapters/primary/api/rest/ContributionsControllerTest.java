package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import java.util.List;

import org.jqassistant.tooling.dashboard.api.dto.ContributionDTO;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.ContributionMapper;
import org.jqassistant.tooling.dashboard.service.application.ContributionService;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jqassistant.tooling.dashboard.service.ModelFixture.stubContribution;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContributionController.class)
@ExtendWith(MockitoExtension.class)
class ContributorControllerTest {

    @MockitoBean
    private ContributionService contributionService;

    @MockitoBean
    private ContributionMapper contributionMapper;

    @Captor
    private ArgumentCaptor<ProjectKey> projectKeyArgumentCaptor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void setContributors() throws Exception {
        Contributions contributions = stubContribution();
        doReturn(contributions).when(contributionMapper)
            .toContribution(any(ContributionDTO.class), any(ProjectKey.class), anyString());

        mockMvc.perform(put("/api/rest/v1/jqassistant/plugins/test/contributions").content("""
                    [
                      { "ident": "MaxMustermann" }
                    ]
                    """)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(contributionMapper).toContribution(any(ContributionDTO.class), any(ProjectKey.class), eq("test"));
        verify(contributionService).setContributions(projectKeyArgumentCaptor.capture(), eq("test"), eq(List.of(contributions)));
        ProjectKey projectKey = projectKeyArgumentCaptor.getValue();
        assertThat(projectKey.getOwner()).isEqualTo("jqassistant");
        assertThat(projectKey.getProject()).isEqualTo("plugins");

    }

}
