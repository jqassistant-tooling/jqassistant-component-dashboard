package org.jqassistant.tooling.dashboard.service.application.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import org.jqassistant.tooling.dashboard.service.application.model.Project.ContainsComponent;

@Label
public interface Component extends NameTemplate {

    @Incoming
    @ContainsComponent
    Project getProject();

    @HasVersion
    List<Version> getVersions();

    @Relation("HAS_LATEST_VERSION")
    Version getLatestVersion();

    void setLatestVersion(Version version);

    @Relation("HAS_VERSION")
    @interface HasVersion {
    }
}
