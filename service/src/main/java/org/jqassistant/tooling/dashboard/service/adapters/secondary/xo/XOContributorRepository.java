package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.ContributorSummary;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.jqassistant.tooling.dashboard.service.application.model.Project;

import java.util.stream.Stream;

@Repository
public interface XOContributorRepository {

    @ResultOf
    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_COMPONENT]->(component:Component {name:$componentId})
            <-[:TO_COMPONENT]-(contrib:Contributions)<-[:CONTRIBUTED]-(contributor:Contributor)
        WHERE
          id(project) = $project
        RETURN
          contributor AS contributor,
          contrib AS contributions
        ORDER BY
          contributor.identString
        """)
    Stream<ContributorSummary> getContributorSummaries(Project project, String componentId);

    @ResultOf
    @Cypher("""
        MERGE
          (contributor:Contributor {identString:$identString})
        RETURN
          contributor
        """)
    Contributor resolveContributor(String identString);

}
