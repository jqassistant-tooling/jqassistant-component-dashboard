package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;
import org.jqassistant.tooling.dashboard.service.application.ContributionSummary;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.jqassistant.tooling.dashboard.service.application.model.Project;

import java.util.stream.Stream;

@Repository
public interface XOContributionsRepository {


    //TODO checken ob Contributions eigene ID brauchen, bzw componentID und contributor-ident
    @ResultOf
    @Cypher("""
            MERGE
              (project:Project{name: $project})
            MERGE
              (project)-[:CONTAINS_COMPONENT]->(comp:Component{name: $componentID})
            MERGE
              (c:Contributor {identString:$ident})
            MERGE
              (c)-[:CONTRIBUTED]->(contrib:Contributions)-[:TO_COMPONENT]->(comp)
            RETURN
              contrib
        """)
    Contributions resolveContribution(String project, String componentID, String ident);

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
    Stream<ContributionSummary> getContributionSummaries(Project project, String componentId);
}
