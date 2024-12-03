package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.model.Project;

@Repository
public interface XOProjectRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (owner:Owner{id:$owner})
        MERGE
          (owner)-[:OWNS_PROJECT]->(project:Project{name:$project})
        RETURN
          project
        """)
    Project findProject(@Parameter("owner") String owner, @Parameter("project") String project);
}
