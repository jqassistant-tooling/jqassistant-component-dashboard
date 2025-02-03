package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.neo4j.api.annotation.Cypher;

import org.jqassistant.tooling.dashboard.service.application.CapabilityRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Capability;
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

    /**
     * The filter to be used for {@link #findAll(Project, Set, List, int, int)} and {@link #countAll(Project, Set, List)}
     */
    String CAPABILITY_FILTER = """
        MATCH
          (project:Project)-[:CONTAINS_CAPABILITY]->(capability:Capability)
        WHERE
          id(project) = $project
          and ($typeFilter is null or isEmpty($typeFilter) or capability.type in $typeFilter)
          and (isEmpty($valueFilter) or all(token in $valueFilter where toLower(capability.value) contains toLower(token)))
        """;

    @ResultOf
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
    Stream<CapabilityRepository.CapabilitySummary> findAll(Project project, Set<String> typeFilter, List<String> valueFilter, int offset, int limit);

    @ResultOf
    @Cypher(CAPABILITY_FILTER + """
        RETURN
          count(capability)
        """)
    int countAll(Project project, Set<String> typeFilter, List<String> valueFilter);

    @ResultOf
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
    List<String> getTypes(Project project);

    @ResultOf
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
    List<CapabilityRepository.Dependencies> getRequiredBy(Project project, String type, String value);

    @ResultOf
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
    List<CapabilityRepository.Dependencies> getProvidedBy(Project project, String type, String value);

}
