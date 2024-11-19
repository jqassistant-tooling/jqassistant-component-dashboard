package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.stream.Stream;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.XOManager;

import lombok.RequiredArgsConstructor;

import static java.util.stream.StreamSupport.stream;
import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public class AbstractXORepository<T> {

    protected final XOManager xoManager;

    private final Class<T> repositoryClass;

    protected final T getXORepository() {
        return xoManager.getRepository(repositoryClass);
    }

    protected static <T> Stream<T> toStream(ResultIterable<T> result) {
        return stream(result.spliterator(), false);
    }

}
