package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;

@Repository
public interface XOContributorRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (contributor:Contributor {identString:$identString})
        RETURN
          contributor
        """)
    Contributor resolveContributor(String identString);

}
