package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label
public interface Component extends Dashboard, CapabilitiesTemplate {

    String getId();

    void setId(String id);

    @Relation("HAS_VERSION")
    List<Version> getVersions();


}
