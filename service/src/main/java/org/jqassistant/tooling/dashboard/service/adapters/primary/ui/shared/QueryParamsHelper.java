package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Location;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.stream;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
public class QueryParamsHelper {

    private final Location location;

    public QueryParamsHelper withParameter(String parameterName, Consumer<String> consumer) {
        location.getQueryParameters().getSingleParameter(parameterName)
            .ifPresent(consumer::accept);
        return this;
    }

    public QueryParamsHelper withParameters(String parameterName, Consumer<List<String>> consumer) {
        List<String> values = location.getQueryParameters().getParameters(parameterName);
        if (isNotEmpty(values)) {
            consumer.accept(values);
        }
        return this;
    }

    public void update(Optional<UI> optionalUI, Consumer<UriComponentsBuilder> consumer) {
        optionalUI.ifPresent(ui -> {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().path(location.getPath());
            consumer.accept(uriComponentsBuilder);
            ui.getPage()
                .getHistory()
                .replaceState(null, uriComponentsBuilder.encode()
                    .build()
                    .toString());
        });
    }


    public static String join(List<String> value) {
        return String.join(" ", value);
    }

    public static List<String> split(String value) {
        return stream(StringUtils.split(value)).toList();
    }

}
