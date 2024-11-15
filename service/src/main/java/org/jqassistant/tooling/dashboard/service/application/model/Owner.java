package org.jqassistant.tooling.dashboard.service.application.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label
public interface Owner extends NameTemplate {

    @OwnsProject
    List<Project> getProjects();

    @Relation("OWNS_PROJECT")
    @interface OwnsProject {}

}
