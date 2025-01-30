package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import lombok.RequiredArgsConstructor;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class RouteParametersHelper {

    private final List<RouteParam> routeParams = new ArrayList<>();

    public static RouteParametersHelper builder() {
        return new RouteParametersHelper();
    }

    public RouteParametersHelper withParameter(String name, String value) {
        this.routeParams.add(new RouteParam(name, encode(value, UTF_8)));
        return this;
    }

    public RouteParameters build() {
        return new RouteParameters(this.routeParams.toArray(new RouteParam[0]));
    }

    public static String get(RouteParameters routeParameters, String name) {
        return URLDecoder.decode(routeParameters.get(name)
            .orElseThrow(), UTF_8);
    }
}
