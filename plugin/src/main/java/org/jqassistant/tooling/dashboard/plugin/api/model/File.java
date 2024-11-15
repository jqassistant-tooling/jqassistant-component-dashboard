package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label
public interface File extends Dashboard, FileDescriptor {

    @Relation("PROVIDES_CAPABILITY")
    List<Capability> getProvidesCapabilities();

    @Relation("REQUIRES_CAPABILITY")
    List<Capability> getRequiresCapabilities();
}
