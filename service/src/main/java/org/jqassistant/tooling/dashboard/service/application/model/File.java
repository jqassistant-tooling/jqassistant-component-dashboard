package org.jqassistant.tooling.dashboard.service.application.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label
public interface File {

    String getFileName();

    void setFileName(String fileName);

    @Relation("PROVIDES_CAPABILITY")
    List<Capability> getProvidesCapabilities();

    @Relation("REQUIRES_CAPABILITY")
    List<Capability> getRequiresCapabilities();
}
