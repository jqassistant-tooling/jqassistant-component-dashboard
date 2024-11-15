package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.FileRepository;
import org.jqassistant.tooling.dashboard.service.application.model.File;

@Repository
public interface XOFileRepository extends FileRepository {

    @ResultOf
    @Cypher("""
        CREATE
          (f:File)
        RETURN
          f
        """)
    @Override
    File create();

}
