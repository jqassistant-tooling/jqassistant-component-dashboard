package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;

@Repository
public interface XOComponentRepository extends ComponentRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (project:Project{name: $projectId})
        MERGE
          (project)-[:CONTAINS_COMPONENT]->(component:Component{name: $componentId})
        RETURN
          component
        """)
    @Override
    Component resolve(@Parameter("projectId") String projectId, @Parameter("componentId") String componentId);

}
