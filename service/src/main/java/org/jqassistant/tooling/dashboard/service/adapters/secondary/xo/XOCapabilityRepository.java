package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

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

}
