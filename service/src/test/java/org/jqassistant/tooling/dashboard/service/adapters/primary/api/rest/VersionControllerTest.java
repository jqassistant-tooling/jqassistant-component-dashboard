package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest;

import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.auth.AuthenticationFilter;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.config.RestApiProperties;
import org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.mapper.VersionMapper;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.ProjectService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @MockBean
    private RestApiProperties restApiProperties;

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

    @Disabled
    @Test
    void getVersion() throws Exception {
        String apiKey = "secret_token";
        Mockito.doReturn(apiKey)
            .when(restApiProperties)
            .getAuthToken();
        mockMvc.perform(put("/api/rest/v1/jqassistant/plugins/test/1.0.0").header(AuthenticationFilter.AUTH_TOKEN_HEADER_NAME, apiKey)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
