package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.model.Component;

@Repository
public interface XOComponentRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (project:Project{name: $projectId})
        MERGE
          (project)-[:CONTAINS_COMPONENT]->(component:Component{name: $componentId})
        RETURN
          component
        """)
    Component resolve(@Parameter("projectId") String projectId, @Parameter("componentId") String componentId);

    @ResultOf
    @Cypher("""
        MATCH
          (component:Component)
        WHERE
          ($nameFilter is null or toLower(component.name) contains toLower($nameFilter))
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
    ResultIterable<Component> findAll(@Parameter("nameFilter") String nameFilter, @Parameter("offset") int offset, @Parameter("limit") int limit);

    @ResultOf
    @Cypher("""
        MATCH
          (component:Component)
        WHERE
          ($nameFilter is null or toLower(component.name) contains toLower($nameFilter))
        RETURN
          count(component)
        """)
    int countAll(@Parameter("nameFilter") String nameFilter);
}
