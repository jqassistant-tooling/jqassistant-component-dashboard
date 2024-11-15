package org.jqassistant.tooling.dashboard.service.application.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import org.jqassistant.tooling.dashboard.service.application.model.Owner.OwnsProject;

@Label
public interface Project extends NameTemplate {

    @Incoming
    @OwnsProject
    Owner getOwner();

    @ContainsComponent
    List<Component> getComponents();

    @Relation("CONTAINS_COMPONENT")
    @interface ContainsComponent {
    }

}
