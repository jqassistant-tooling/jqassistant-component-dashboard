package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

@Label
public interface Component extends Dashboard, CapabilitiesTemplate {

    String getId();

    void setId(String id);

    @HasVersion
    @Outgoing
    List<Version> getVersions();

    @Relation("HAS_VERSION")
    @interface HasVersion {
    }
}
