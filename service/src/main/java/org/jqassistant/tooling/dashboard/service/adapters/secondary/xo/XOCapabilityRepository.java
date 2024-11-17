package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;

@Repository
public interface XOCapabilityRepository extends CapabilityRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (capability:Capability{type: $type, value: $value})
        RETURN
          capability
        """)
    @Override
    Capability resolve(@Parameter("type") String type, @Parameter("value") String value);

    @ResultOf
    @Cypher("""
        MATCH
          (capability:Capability)
        WHERE
          ($typeFilter is null or toLower(capability.type) contains toLower($typeFilter))
          and ($valueFilter is null or toLower(capability.value) contains toLower($valueFilter))
        WITH
          capability
        ORDER BY
          capability.type, capability.value
        SKIP
          $offset
        RETURN
          capability
        LIMIT
          $limit
        """)
    @Override
    ResultIterable<Capability> findAll(@Parameter("typeFilter") String typeFilter, @Parameter("valueFilter") String valueFilter,
        @Parameter("offset") int offset, @Parameter("limit") int limit);

    @ResultOf
    @Cypher("""
        MATCH
          (capability:Capability)
        WHERE
          ($typeFilter is null or toLower(capability.type) contains toLower($typeFilter))
          and ($valueFilter is null or toLower(capability.value) contains toLower($valueFilter))
        RETURN
          count(capability)
        """)
    @Override
    int countAll(@Parameter("typeFilter") String typeFilter, @Parameter("valueFilter") String valueFilter);
}
