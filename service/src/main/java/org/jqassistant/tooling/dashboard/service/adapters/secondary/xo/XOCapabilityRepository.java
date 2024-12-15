package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;

@Repository
public interface XOCapabilityRepository {

    @ResultOf
    @Cypher("""
        MATCH
          (project:Project)
        WHERE
          id(project) = $project
        MERGE
          (project)-[:CONTAINS_CAPABILITY]->(capability:Capability{type: $type, value: $value})
        RETURN
          capability
        """)
    Capability resolve(Project project, String type, String value);

    @ResultOf
    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability{type: $type, value: $value})
        WHERE
          id(project) = $project
        RETURN
          capability
        """)
    Capability find(Project project, String type, String value);

    String CAPABILITY_FILTER = """
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability)
        WHERE
          id(project) = $project
          and ($typeFilter is null or capability.type in $typeFilter)
          and ($valueFilter is null or toLower(capability.value) contains toLower($valueFilter))
        """;

    @ResultOf
    @Cypher(CAPABILITY_FILTER + """
        RETURN
          count(capability)
        """)
    int countAll(Project project, Set<String> typeFilter, String valueFilter);

    @Cypher(CAPABILITY_FILTER + """
        WITH
          capability
        ORDER BY
          capability.type, capability.value
        SKIP
          $offset
        LIMIT
          $limit
        OPTIONAL MATCH
          (component)-[:HAS_VERSION]->(:Version)-[:CONTAINS_FILE]->(:File)-[:PROVIDES_CAPABILITY]->(capability:Capability)
        RETURN
          capability, collect(distinct component) as providedByComponents
        """)
    interface CapabilitySummary extends CapabilityRepository.CapabilitySummary {

        @Override
        Capability getCapability();

        @Override
        List<Component> getProvidedByComponents();
    }

    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability)
        WHERE
          id(project) = $project
        RETURN
          distinct capability.type as type
        ORDER BY
          type
        """)
    interface Types {
        String getType();
    }

    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability{type: $type, value: $value}),
          (component:Component)-[:HAS_VERSION]->(version)-[:CONTAINS_FILE]->(file:File)-[:REQUIRES_CAPABILITY]->(capability)
        WHERE
          id(project) = $project
        WITH
          component, version, collect(file) as files
        ORDER BY
          component.name, version.updatedAt desc
        RETURN
          component, collect({
            version: version,
            files: files
          }) as versions
        """)
    interface CapabilityRequiredBy extends CapabilityRepository.Dependencies {

        Component getComponent();

        List<Map<String, Object>> getVersions();

    }

    @Cypher("""
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability{type: $type, value: $value}),
          (component:Component)-[:HAS_VERSION]->(version)-[:CONTAINS_FILE]->(file:File)-[:PROVIDES_CAPABILITY]->(capability)
        WHERE
          id(project) = $project
        WITH
          component, version, collect(file) as files
        ORDER BY
          component.name, version.updatedAt desc
        RETURN
          component, collect({
            version: version,
            files: files
          }) as versions
        """)
    interface CapabilityProvidedBy extends CapabilityRepository.Dependencies {

        Component getComponent();

        List<Map<String, Object>> getVersions();

    }

}
