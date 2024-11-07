package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface CapabilitiesTemplate {

    @Relation("PROVIDES_CAPABILITY")
    List<Capability> getProvidesCapabilities();

    @Relation("REQUIRES_CAPABILITY")
    List<Capability> getRequiresCapabilities();

}
