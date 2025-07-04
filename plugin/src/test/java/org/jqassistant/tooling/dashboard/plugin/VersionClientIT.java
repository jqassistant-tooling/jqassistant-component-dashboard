package org.jqassistant.tooling.dashboard.plugin;

import java.time.ZonedDateTime;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jqassistant.tooling.dashboard.api.dto.FileDTO;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.rest.client.RESTClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("For manual execution only")
public class VersionClientIT {

    @Test
    void publishVersion() {
        VersionDTO versionDTO = new VersionDTO();
        versionDTO.setVersion("1.0.2");
        versionDTO.setName("jQAssistant JEE Plugin");
        versionDTO.setUpdatedAt(ZonedDateTime.now());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName("pom.xml");
        versionDTO.setContainsFiles(List.of(fileDTO));

        try (RESTClient restClient = new RESTClient("http://localhost:8080", "secret_api_token", true)) {
            WebTarget versionTarget = restClient.target()
                .path("api")
                .path("rest")
                .path("v1")
                .path("jqassistant")
                .path("plugins")
                .path("jqassistant-jee-plugin")
                .path("versions")
                .path("1.0.0");
            try (Response response = versionTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .put(json(versionDTO))) {
                assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
            }
        }

    }

}
