package org.jqassistant.tooling.dashboard.service.it;

import java.time.ZonedDateTime;

import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.ComponentService;
import org.jqassistant.tooling.dashboard.service.application.VersionService;
import org.jqassistant.tooling.dashboard.service.application.model.ProjectKey;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Disabled("For manual execution only")
public class VersionIT {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private VersionService versionService;

    @Test
    void createVersion() {
        // given
        ProjectKey projectKey = new ProjectKey("jqassistant", "plugins");

        // when
        versionService.createOrUpdate(projectKey, "jqassistant-jee-plugin", "1.0.0", (project, c) -> {
            Version version = versionService.resolve(c, "1.0.0");
            version.setName("jQAssistant JEE Plugin");
            version.setUpdatedAt(ZonedDateTime.now());
            return version;
        });

        // then
        ComponentRepository.ComponentSummary componentSummary = componentService.find(projectKey, "jqassistant-jee-plugin");
        assertThat(componentSummary).isNotNull();
        Version latestVersion = componentSummary.getLatestVersion();
        assertThat(latestVersion.getVersion()).isEqualTo("1.0.0");
    }

}
