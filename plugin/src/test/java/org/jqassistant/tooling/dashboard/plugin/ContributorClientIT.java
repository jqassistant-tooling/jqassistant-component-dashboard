package org.jqassistant.tooling.dashboard.plugin;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.rest.client.RESTClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("For manual execution only")
public class ContributorClientIT {

    @Test
    void publishVersion() {
        ContributorDTO contributorDTO = new ContributorDTO();
        contributorDTO.setName("Tobias Krakau");
        contributorDTO.setEmail("tobias.krakau.berlin@gmail.com");
        contributorDTO.setIdent("tobiaskrakau");


        try (RESTClient restClient = new RESTClient("http://localhost:8080", "secret_api_token", true)) {
            WebTarget versionTarget = restClient.target()
                .path("api")
                .path("rest")
                .path("v1")
                .path("jqassistant")
                .path("plugins")
                .path("jqassistant-jee-plugin")
                .path("contributors");
            try (Response response = versionTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .put(json(List.of(contributorDTO)))) {
                assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
            }
        }

    }

}
