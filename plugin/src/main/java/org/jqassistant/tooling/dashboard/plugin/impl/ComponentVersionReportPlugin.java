package org.jqassistant.tooling.dashboard.plugin.impl;

import java.util.Map;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.buschmais.jqassistant.core.report.api.ReportContext;
import com.buschmais.jqassistant.core.report.api.ReportException;
import com.buschmais.jqassistant.core.report.api.ReportPlugin;
import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Result.Status;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.ExecutableRule;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.api.dto.VersionDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Component;
import org.jqassistant.tooling.dashboard.plugin.api.model.Version;
import org.jqassistant.tooling.dashboard.plugin.impl.mapper.VersionMapper;

import static javax.ws.rs.client.Entity.json;

@Slf4j
public class ComponentVersionReportPlugin implements ReportPlugin {

    public static final String PROPERTY_DASHBOARD_URL = "dashboard.url";
    public static final String PROPERTY_DASHBOARD_OWNER = "dashboard.owner";
    public static final String PROPERTY_DASHBOARD_PROJECT = "dashboard.project";
    public static final String PROPERTY_DASHBOARD_API_KEY = "dashboard.api-key";

    private static final String CONCEPT_ID = "jqassistant-dashboard:ComponentVersionReport";
    private static final String COLUMN_COMPONENT = "Component";
    private static final String COLUMN_VERSION = "Version";

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private Client client;

    private Optional<WebTarget> optionalApiTarget;

    private String apiKey;

    @Override
    public void initialize() {
        this.client = ClientBuilder.newClient()
            .register(JacksonJsonProvider.class)
            .register(ObjectMapperContextResolver.class);
    }

    @Override
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void configure(ReportContext reportContext, Map<String, Object> properties) {
        String url = (String) properties.get(PROPERTY_DASHBOARD_URL);
        String owner = (String) properties.get(PROPERTY_DASHBOARD_OWNER);
        String project = (String) properties.get(PROPERTY_DASHBOARD_PROJECT);
        this.apiKey = (String) properties.get(PROPERTY_DASHBOARD_API_KEY);
        if (url == null || owner == null || project == null || this.apiKey == null) {
            log.info("Dashboard URL, owner, project or API key not configured, skipping.");
        } else {
            log.info("Using dashboard at '{}' (owner='{}', project='{}').", url, owner, project);
            this.optionalApiTarget = Optional.of(client.target(url)
                .path("api")
                .path("rest")
                .path("v1")
                .path(owner)
                .path(project));
        }
    }

    @Override
    public void setResult(Result<? extends ExecutableRule> result) throws ReportException {
        ExecutableRule<?> rule = result.getRule();
        if (!rule.getId()
            .equals(CONCEPT_ID)) {
            throw new ReportException("The rule id is not " + CONCEPT_ID);
        }
        if (!result.getStatus()
            .equals(Status.SUCCESS)) {
            log.warn("The concept '{}' did returned status {}, report will not be published.", rule.getId(), result.getStatus());
        } else {
            publishVersions(result);
        }
    }

    private void publishVersions(Result<? extends ExecutableRule> result) throws ReportException {
        for (Row row : result.getRows()) {
            Column<Component> componentColumn = getColumn(row, COLUMN_COMPONENT);
            Column<Version> versionColumn = getColumn(row, COLUMN_VERSION);
            publish(versionColumn, componentColumn);
        }
    }

    private void publish(Column<Version> versionColumn, Column<Component> componentColumn) {
        Version version = versionColumn.getValue();
        VersionDTO versionDTO = VersionMapper.MAPPER.toDTO(version);
        optionalApiTarget.ifPresent(apiTarget -> {
            Component component = componentColumn.getValue();
            WebTarget versionTarget = apiTarget.path(component.getId())
                .path(version.getVersion());
            try (Response put = versionTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(AUTH_TOKEN_HEADER_NAME, apiKey)
                .put(json(versionDTO))) {
                log.info("Component '{}' version '{}' published to '{}' (status={}).", component.getId(), version.getVersion(), versionTarget.getUri(),
                    put.getStatus());
            }
        });
    }

    private static <T> Column<T> getColumn(Row row, String columnName) throws ReportException {
        Column<T> column = (Column<T>) row.getColumns()
            .get(columnName);
        if (column == null) {
            throw new ReportException("The row does not contain the required column " + columnName);
        }
        return column;
    }
}
