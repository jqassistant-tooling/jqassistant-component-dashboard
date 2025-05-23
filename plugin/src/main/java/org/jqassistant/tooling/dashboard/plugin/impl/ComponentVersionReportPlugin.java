package org.jqassistant.tooling.dashboard.plugin.impl;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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

import static java.lang.Boolean.parseBoolean;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.client.Entity.json;

@Slf4j
public class ComponentVersionReportPlugin implements ReportPlugin {

    public static final String PROPERTY_DASHBOARD_URL = "dashboard.url";
    public static final String PROPERTY_DASHBOARD_OWNER = "dashboard.owner";
    public static final String PROPERTY_DASHBOARD_PROJECT = "dashboard.project";
    public static final String PROPERTY_DASHBOARD_API_KEY = "dashboard.api-key";
    public static final String PROPERTY_DASHBOARD_SSL_VALIDATION = "dashboard.ssl-validation";

    private static final String CONCEPT_ID = "jqassistant-dashboard:ComponentVersionReport";
    private static final String COLUMN_COMPONENT = "Component";
    private static final String COLUMN_VERSION = "Version";

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
            .equals(Status.SUCCESS)) {
            log.warn("The concept '{}' did returned status {}, report will not be published.", rule.getId(), result.getStatus());
        } else if (url == null || owner == null || project == null || this.apiKey == null) {
            log.info("Dashboard URL, owner, project or API key not configured, skipping.");
        } else {
            publishVersions(result);
        }
    }

    private void publishVersions(Result<? extends ExecutableRule> result) throws ReportException {
        log.info("Publishing to dashboard at '{}' (owner='{}', project='{}').", url, owner, project);
        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
            .register(JacksonJsonProvider.class)
            .register(ObjectMapperContextResolver.class);
        if (!sslValidation) {
            log.warn("SSL validation is disabled.");
            clientBuilder.sslContext(getNoopSSLContext());
        }
        Client client = clientBuilder.build();
        try {
            WebTarget apiTarget = client.target(url)
                .path("api")
                .path("rest")
                .path("v1")
                .path(owner)
                .path(project);
            for (Row row : result.getRows()) {
                Column<Component> componentColumn = getColumn(row, COLUMN_COMPONENT);
                Column<Version> versionColumn = getColumn(row, COLUMN_VERSION);
                publish(versionColumn, componentColumn, apiTarget);
            }
        } finally {
            client.close();
        }
    }

    private SSLContext getNoopSSLContext() throws ReportException {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        TrustManager[] noopTrustManager = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("ssl");
            sc.init(null, noopTrustManager, null);
        } catch (GeneralSecurityException e) {
            throw new ReportException("Cannot initialize NOOP SSL context", e);
        }
        return sc;
    }

    private void publish(Column<Version> versionColumn, Column<Component> componentColumn, WebTarget apiTarget) {
        Version version = versionColumn.getValue();
        VersionDTO versionDTO = VersionMapper.MAPPER.toDTO(version);
        Component component = componentColumn.getValue();
        WebTarget versionTarget = apiTarget.path(encode(component.getId(), UTF_8))
            .path("versions")
            .path(encode(version.getVersion(), UTF_8));
        try (Response put = versionTarget.request(MediaType.APPLICATION_JSON_TYPE)
            .header(AUTH_TOKEN_HEADER_NAME, apiKey)
            .put(json(versionDTO))) {
            log.info("Component '{}' version '{}' published to '{}' (status={}).", component.getId(), version.getVersion(), versionTarget.getUri(),
                put.getStatus());
        }
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
