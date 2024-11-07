package org.jqassistant.tooling.dashboard.plugin.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label
public interface Capability extends Dashboard {

    String getType();

    void setType(String type);

    String getValue();

    void setValue(String value);

}
