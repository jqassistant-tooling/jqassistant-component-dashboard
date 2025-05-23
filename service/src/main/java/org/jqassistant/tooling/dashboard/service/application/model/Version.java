package org.jqassistant.tooling.dashboard.service.application.model;

import java.time.ZonedDateTime;
import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import org.jqassistant.tooling.dashboard.service.application.model.Component.HasVersion;

@Label
public interface Version extends NameTemplate, DescriptionTemplate {

    String getVersion();

    void setVersion(String version);

    String getUrl();

    void setUrl(String url);

    ZonedDateTime getUpdatedAt();

    void setUpdatedAt(ZonedDateTime updatedAt);

    @Relation("CONTAINS_FILE")
    List<File> getContainsFiles();

    @Incoming
    @HasVersion
    Component getComponent();

}
