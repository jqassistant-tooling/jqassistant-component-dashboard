package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import org.jqassistant.tooling.dashboard.plugin.api.model.Component.HasVersion;

@Label
public interface Version extends Dashboard {

    String getVersion();

    void setVersion(String version);

    @HasVersion
    @Incoming
    Component getComponent();

    @Relation("CONTAINS_FILE")
    List<File> getContainsFiles();

}
