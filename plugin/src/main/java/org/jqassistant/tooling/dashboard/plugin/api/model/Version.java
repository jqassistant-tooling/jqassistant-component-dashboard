package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.time.ZonedDateTime;
import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import org.jqassistant.tooling.dashboard.plugin.api.model.Component.HasVersion;

@Label
public interface Version extends Dashboard {

    String getVersion();

    void setVersion(String version);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    ZonedDateTime getUpdatedAt();

    void setUpdatedAt(ZonedDateTime lastUpdatedAt);

    @HasVersion
    @Incoming
    Component getComponent();

    @Relation("CONTAINS_FILE")
    List<File> getContainsFiles();

}
