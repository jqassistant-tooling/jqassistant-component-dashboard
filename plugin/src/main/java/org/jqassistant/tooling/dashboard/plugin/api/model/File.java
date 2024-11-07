package org.jqassistant.tooling.dashboard.plugin.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label
public interface File extends Dashboard {

    String getPath();

    void setPath(String localPath);
}
