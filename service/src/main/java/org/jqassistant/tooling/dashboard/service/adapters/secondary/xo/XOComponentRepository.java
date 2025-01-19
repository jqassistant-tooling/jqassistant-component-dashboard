package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.Version;

import java.util.List;

@Repository
public interface XOComponentRepository {

    @ResultOf
    @Cypher("""
        MERGE
          (project:Project{name: $project})
        MERGE
          (project)-[:CONTAINS_COMPONENT]->(component:Component{name: $component})
        RETURN
          component
        """)
    Component resolve(String project, String component);

    String COMPONENT_FILTER = """
        MATCH
          (project:Project)-[:CONTAINS_COMPONENT]->(component:Component)-[:HAS_LATEST_VERSION]->(latestVersion:Version)
        WHERE
          id(project) = $project
          and (isEmpty($nameFilter) or all(token in $nameFilter where toLower(latestVersion.name) contains toLower(token)))
          and (isEmpty($descriptionFilter) or all(token in $descriptionFilter where toLower(latestVersion.description) contains toLower(token)))
        """;

    @Cypher(COMPONENT_FILTER + """
        WITH
          component, latestVersion
        ORDER BY
          component.name
        SKIP
          $offset
        MATCH
          (component)-[:HAS_VERSION]->(version:Version)
        RETURN
          component, latestVersion, count(version) as versionCount
        LIMIT
          $limit
        """)
    interface XOComponentSummary extends ComponentRepository.ComponentSummary {

        @Override
        Component getComponent();

        @Override
        Version getLatestVersion();

        Long getVersionCount();
    }

    @ResultOf
    @Cypher(COMPONENT_FILTER + """
        RETURN
          count(component)
        """)
    int countAll(Project project, List<String> nameFilter, List<String> descriptionFilter);
}
