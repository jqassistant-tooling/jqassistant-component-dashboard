package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

@Label
public interface Contributor {

    String getName();
    void setName(String name);

    @Comitted
    @Outgoing
    List<Component> getContributedComponents();

    @Relation("Comitted")
    @interface Comitted {
    }

}
