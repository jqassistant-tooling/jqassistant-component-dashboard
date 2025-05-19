package org.jqassistant.tooling.dashboard.service.application.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label
public interface Capability extends DescriptionTemplate {

    String getType();

    void setType(String type);

    String getValue();

    void setValue(String value);

}
