package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;

import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public class AbstractXORepository<T> {

    protected final XOManager xoManager;

    private final Class<T> repositoryClass;

    protected final T getXORepository() {
        return xoManager.getRepository(repositoryClass);
    }

}
