package org.jqassistant.tooling.dashboard.service.application.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

@Label("Contributions")
public interface Contributions {

    long getCommits();
    void setCommits(long commits);

    @Relation("TO_COMPONENT")
    @Outgoing
    Component getToComponent();
    void setToComponent(Component component);
}
