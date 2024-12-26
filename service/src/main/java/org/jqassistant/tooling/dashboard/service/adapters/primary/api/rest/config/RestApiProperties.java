package org.jqassistant.tooling.dashboard.service.adapters.primary.api.rest.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "api.rest")
public class RestApiProperties {

    private String authToken;

}
