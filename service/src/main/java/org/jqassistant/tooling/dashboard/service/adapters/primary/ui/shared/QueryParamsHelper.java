package org.jqassistant.tooling.dashboard.service.adapters.primary.ui.shared;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class QueryParamsHelper<F> {

    private final Location location;

    private final F filter;

    public QueryParamsHelper<F> withSingleParameter(String parameterName, Consumer<String> consumer) {
        location.getQueryParameters().getSingleParameter(parameterName)
            .ifPresent(value -> {
                consumer.accept(value);
            });
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

}
