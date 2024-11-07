package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label
public interface Version extends Dashboard {

    String getVersion();

    void setVersion(String version);

    @Relation("CONTAINS_FILE")
    List<File> getFiles();

}
