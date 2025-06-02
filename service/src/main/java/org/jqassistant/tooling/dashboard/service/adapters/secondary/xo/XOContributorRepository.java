package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;
import org.jqassistant.tooling.dashboard.service.application.model.Project;

import java.util.stream.Stream;

@Repository
public interface XOContributorRepository {

    @ResultOf
    @Cypher("""
        with ["Dirk", "Max", "Leonard", "Toby", "Daniel"] as contributors
        unwind contributors as contributor
        return contributor
        """)

    Stream<String> getContributors(Project project, String componentId);
}
