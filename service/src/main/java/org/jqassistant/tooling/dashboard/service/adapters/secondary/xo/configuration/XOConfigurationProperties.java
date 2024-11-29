package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "xo.neo4j")
public class XOConfigurationProperties {

    private String url;
    private String username;
    private String password;

}
