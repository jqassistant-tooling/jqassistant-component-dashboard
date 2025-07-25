package org.jqassistant.tooling.dashboard.plugin.impl;

import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.buschmais.jqassistant.core.report.api.*;
import com.buschmais.jqassistant.core.report.api.model.*;
import com.buschmais.jqassistant.core.rule.api.model.ExecutableRule;

import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import lombok.extern.slf4j.Slf4j;

import org.jqassistant.tooling.dashboard.api.dto.ContributorDTO;
import org.jqassistant.tooling.dashboard.plugin.api.model.Component;
import org.jqassistant.tooling.dashboard.plugin.impl.mapper.ContributorMapper;
import org.jqassistant.tooling.dashboard.rest.client.RESTClient;

import static java.lang.Boolean.parseBoolean;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.client.Entity.json;

@Slf4j
public class ContributorsReportPlugin implements ReportPlugin {

    public static final String PROPERTY_DASHBOARD_URL = "dashboard.url";
    public static final String PROPERTY_DASHBOARD_OWNER = "dashboard.owner";
    public static final String PROPERTY_DASHBOARD_PROJECT = "dashboard.project";
    public static final String PROPERTY_DASHBOARD_API_KEY = "dashboard.api-key";
    public static final String PROPERTY_DASHBOARD_SSL_VALIDATION = "dashboard.ssl-validation";

    private static final String CONCEPT_ID = "jqassistant-dashboard-git:ComponentContributorsReport";
    private static final String COLUMN_COMPONENT = "Component";
    private static final String COLUMN_AUTHOR = "Author";

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private String url;
    private String owner;
    private String project;
    private String apiKey;
    private boolean sslValidation;

    @Override
    public void configure(ReportContext reportContext, Map<String, Object> properties) {
        this.url = (String) properties.get(PROPERTY_DASHBOARD_URL);
        this.owner = (String) properties.get(PROPERTY_DASHBOARD_OWNER);
        this.project = (String) properties.get(PROPERTY_DASHBOARD_PROJECT);
        this.apiKey = (String) properties.get(PROPERTY_DASHBOARD_API_KEY);
        this.sslValidation = parseBoolean((String) properties.getOrDefault(PROPERTY_DASHBOARD_SSL_VALIDATION, "true"));
    }

    @Override
    public void setResult(Result<? extends ExecutableRule> result) throws ReportException {
        ExecutableRule<?> rule = result.getRule();
        if (!rule.getId()
            .equals(CONCEPT_ID)) {
            throw new ReportException("The rule id is not " + CONCEPT_ID);
        }
        if (!result.getStatus()
            .equals(Result.Status.SUCCESS)) {
            log.warn("The concept '{}' returned status {}, report will not be published.", rule.getId(), result.getStatus());
        } else if (url == null || owner == null || project == null || apiKey == null) {
            log.info("Dashboard URL, owner, project or API key not configured, skipping.");
        } else {
            publishContributors(result);
        }
    }

    private void publishContributors(Result<? extends ExecutableRule> result) throws ReportException {
        log.info("Publishing contributors to dashboard at '{}' (owner='{}', project='{}').", url, owner, project);

        try (RESTClient restClient = new RESTClient(url, apiKey, sslValidation)) {
            WebTarget target = restClient.target()
                .path("api")
                .path("rest")
                .path("v1")
                .path(owner)
                .path(project);

            Map<String, List<ContributorDTO>> groupedContributors = new HashMap<>();

            for (Row row : result.getRows()) {
                Column<Component> componentColumn = getColumn(row, COLUMN_COMPONENT);
                Column<GitAuthorDescriptor> authorColumn = getColumn(row, COLUMN_AUTHOR);

                Component comp = componentColumn.getValue();
                GitAuthorDescriptor contrib = authorColumn.getValue();
                ContributorDTO dto = ContributorMapper.MAPPER.toDTO(contrib);

                groupedContributors
                    .computeIfAbsent(comp.getId(), k -> new ArrayList<>())
                    .add(dto);
            }

            for (Map.Entry<String, List<ContributorDTO>> entry : groupedContributors.entrySet()) {
                String componentId = entry.getKey();
                List<ContributorDTO> contributors = entry.getValue();

                WebTarget apiTarget = target
                    .path(encode(componentId, UTF_8))
                    .path("contributors");

                try (Response response = apiTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .header(AUTH_TOKEN_HEADER_NAME, apiKey)
                    .put(json(contributors))) {

                    log.info("Published {} contributors for component '{}', status: {}",
                        contributors.size(), componentId, response.getStatus());
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> Column<T> getColumn(Row row, String columnName) throws ReportException {
        Column<T> column = (Column<T>) row.getColumns().get(columnName);
        if (column == null) {
            throw new ReportException("Missing required column: " + columnName);
        }
        return column;
    }

}
