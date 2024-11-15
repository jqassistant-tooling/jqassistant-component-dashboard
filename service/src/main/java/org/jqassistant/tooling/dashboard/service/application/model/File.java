package org.jqassistant.tooling.dashboard.service.application.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label
public interface File {

    String getFileName();

    void setFileName(String fileName);

}
