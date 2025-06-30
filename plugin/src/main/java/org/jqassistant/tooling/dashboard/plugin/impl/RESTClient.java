package org.jqassistant.tooling.dashboard.plugin.impl;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.*;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a client for the REST API of the dashboard service.
 * <p>
 * It provides support for authentication using an API key and disabling SSL validation.
 */
@Slf4j
public class RESTClient implements AutoCloseable {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private Client client;

    private WebTarget target;

    public RESTClient(String url, String apiKey, boolean sslValidation) {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
            .register(JacksonJsonProvider.class)
            .register(ObjectMapperContextResolver.class)
            .register(new AuthenticationRequestFilter(apiKey));

        if (!sslValidation) {
            log.warn("SSL validation is disabled.");
            clientBuilder.sslContext(getNoopSSLContext());
        }
        this.client = clientBuilder.build();
        this.target = client.target(url);
    }

    @Override
    public void close() {
        client.close();
    }

    /**
     * Return the {@link WebTarget} representing the configured url.
     *
     * @return The {@link WebTarget}.
     */
    public WebTarget target() {
        return target;
    }

    private SSLContext getNoopSSLContext() {
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
            throw new IllegalStateException("Cannot initialize NOOP SSL context", e);
        }
        return sc;
    }

    @Provider
    @RequiredArgsConstructor
    public class AuthenticationRequestFilter implements ClientRequestFilter {

        private final String apiKey;

        @Override
        public void filter(ClientRequestContext clientRequestContext) {
            clientRequestContext.getHeaders()
                .add(AUTH_TOKEN_HEADER_NAME, apiKey);

        }
    }
}
