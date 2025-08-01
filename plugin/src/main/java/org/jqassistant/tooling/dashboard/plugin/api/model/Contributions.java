package org.jqassistant.tooling.dashboard.plugin.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

@Label("Contributions")
public interface Contributions {

    int getCommits();
    void setCommits(int commits);

    @ToComponent
    @Outgoing
    Component getToComponent();
    void setToComponent(Component component);

    @FromContributor
    @Incoming
    Contributor getContributor();
    void setContributor(Contributor contributor);

    @Relation("TO_COMPONENT")
    @interface ToComponent {}

    @Relation("CONTRIBUTED")
    @interface FromContributor {}
}
