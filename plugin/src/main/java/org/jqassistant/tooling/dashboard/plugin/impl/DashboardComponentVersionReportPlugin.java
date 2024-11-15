package org.jqassistant.tooling.dashboard.plugin.impl;

import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

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

@Slf4j
public class DashboardComponentVersionReportPlugin implements ReportPlugin {

    private static final String CONCEPT_ID = "jqassistant-dashboard:ComponentVersionReport";
    private static final String COLUMN_COMPONENT = "Component";
    private static final String COLUMN_VERSION = "Version";

    private String url;
    private String owner;
    private String project;
    WebTarget target;

    @Override
    public void configure(ReportContext reportContext, Map<String, Object> properties) {
        this.url = "http://localhost:8080/api/rest/v1/";
        this.owner = "jqassistant";
        this.project = "plugins";
        this.target = ClientBuilder.newClient()
            .register(JacksonJsonProvider.class)
            .target(url)
            .path(owner)
            .path(project);
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
            log.warn("The concept '{}' did not return with status {}, report will not be published.", rule.getId(), result.getStatus());
        } else {
            publishVersions(result);
        }
    }

    private void publishVersions(Result<? extends ExecutableRule> result) throws ReportException {
        for (Row row : result.getRows()) {
            Column<Component> componentColumn = getColumn(row, COLUMN_COMPONENT);
            Column<Version> versionColumn = getColumn(row, COLUMN_VERSION);
            Version version = versionColumn.getValue();
            VersionDTO versionDTO = VersionMapper.MAPPER.toDTO(version);
            publish(componentColumn, version, versionDTO);
        }
    }

    private void publish(Column<Component> componentColumn, Version version, VersionDTO versionDTO) {
        WebTarget target = this.target.path(componentColumn.getValue()
                .getId())
            .path(version.getVersion());
        int status = target.request(MediaType.APPLICATION_JSON_TYPE)
            .put(Entity.json(versionDTO))
            .getStatus();
        System.out.println(status);
    }

    private static <T> Column<T> getColumn(Row row, String columnName) throws ReportException {
        Column<T> column = (Column<T>) row.getColumns()
            .get(columnName);
        if (column == null) {
            throw new ReportException("The row does not contain a the required column " + columnName);
        }
        return column;
    }
}
