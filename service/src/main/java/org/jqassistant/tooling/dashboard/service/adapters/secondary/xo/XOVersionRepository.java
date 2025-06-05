package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;

@Repository
public interface XOVersionRepository {

    @ResultOf
    @Cypher("""
        MATCH
          (c:Component)-[:HAS_VERSION]->(v:Version)
        WHERE
          id(c) = $component
          and v.version = $version
        WITH
          v
        OPTIONAL MATCH
          (v)-[:CONTAINS_FILE]->(f:File)
        DETACH DELETE
          f
        DETACH DELETE
          v
        """)
    void remove(Component component, String version);

    @ResultOf
    @Cypher("""
        MATCH
          (c:Component)
        WHERE
          id(c) = $component
        MERGE
          (c)-[:HAS_VERSION]->(v:Version{version: $version})
        RETURN
          v
        """)
    Version create(Component component, String version);
}
