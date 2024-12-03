package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;

@Repository
public interface XOComponentRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (project:Project{name: $project})
        MERGE
          (project)-[:CONTAINS_COMPONENT]->(component:Component{name: $component})
        RETURN
          component
        """)
    Component resolve(@Parameter("project") String project, @Parameter("component") String componentId);

    @ResultOf
    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_COMPONENT]->(component:Component)
        WHERE
          id(project) = $project
          and ($nameFilter is null or toLower(component.name) contains toLower($nameFilter))
        WITH
          component
        ORDER BY
          component.name
        SKIP
          $offset
        RETURN
          component
        LIMIT
          $limit
        """)
    ResultIterable<Component> findAll(@Parameter("project") Project project, @Parameter("nameFilter") String nameFilter, @Parameter("offset") int offset,
        @Parameter("limit") int limit);

    @ResultOf
    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_COMPONENT]->(component:Component)
        WHERE
          id(project) = $project
          and ($nameFilter is null or toLower(component.name) contains toLower($nameFilter))
        RETURN
          count(component)
        """)
    int countAll(@Parameter("project") Project project, @Parameter("nameFilter") String nameFilter);
}
