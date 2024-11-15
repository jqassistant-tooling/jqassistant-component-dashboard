package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.VersionRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Version;

@Repository
public interface XOVersionRepository extends VersionRepository {

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
    @Override
    void remove(@Parameter("component") Component component, @Parameter("version") String version);

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
    @Override
    Version create(@Parameter("component") Component component, @Parameter("version") String version);
}
