package org.jqassistant.tooling.dashboard.projects.jqassistant.plugins.it;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.java.api.model.JavaClassesDirectoryDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.maven3.api.model.EffectiveDescriptor;
import com.buschmais.jqassistant.plugin.maven3.api.model.MavenMainArtifactDescriptor;
import com.buschmais.jqassistant.plugin.maven3.api.model.MavenPomXmlDescriptor;

import org.jqassistant.tooling.dashboard.plugin.api.model.Capability;
import org.jqassistant.tooling.dashboard.projects.jqassistant.plugins.it.model.TestLanguage;
import org.jqassistant.tooling.dashboard.projects.jqassistant.plugins.it.model.TestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jqassistant.tooling.dashboard.plugin.assertj.CapabilityCondition.capability;

class RulesIT extends AbstractJavaPluginIT {

    private static final String GROUP = "org.jqassistant.plugin";
    private static final String NAME = "test";
    private static final String TYPE = "jar";
    private static final String VERSION = "1.0.0";
    private static final String ARTIFACT_ID = GROUP + ":" + NAME + ":" + TYPE + ":" + VERSION;
    private static final String FILENAME = NAME + "." + TYPE;

    @Override
    protected File getRuleDirectory() {
        return new File("jqassistant");
    }

    @BeforeEach
    void reset() {
        store.reset();
        createPluginArtifact();
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-plugin.xml");
    }

    private void createPluginArtifact() {
        store.beginTransaction();
        JavaClassesDirectoryDescriptor javaArtifact = store.create(JavaClassesDirectoryDescriptor.class);
        MavenMainArtifactDescriptor mavenMainArtifact = store.addDescriptorType(javaArtifact, MavenMainArtifactDescriptor.class);
        mavenMainArtifact.setGroup(GROUP);
        mavenMainArtifact.setName(NAME);
        mavenMainArtifact.setVersion(VERSION);
        mavenMainArtifact.setType(TYPE);
        mavenMainArtifact.setFullQualifiedName(ARTIFACT_ID);
        mavenMainArtifact.setFileName(FILENAME);
        MavenPomXmlDescriptor pomXmlDescriptor = store.create(MavenPomXmlDescriptor.class);
        store.addDescriptorType(pomXmlDescriptor, EffectiveDescriptor.class);
        pomXmlDescriptor.getDescribes()
                .add(mavenMainArtifact);
        store.commitTransaction();
    }

    @Test
    void ArtifactProvidesVersion() throws RuleException {
        Result<Concept> result = applyConcept("jqassistant-dashboard-jqassistant-plugins:ArtifactProvidesVersion");
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void pluginProvidesLabel() throws RuleException {
        scanClasses(ARTIFACT_ID, TestLanguage.class, TestType.class);

        Result<Concept> result = applyConcept("jqassistant-dashboard-jqassistant-plugins:PluginProvidesLabel");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(3);
        TestResult testResult = query("MATCH (type:Type)-[:PROVIDES_CAPABILITY]->(capability:Capability) " + //
                "WHERE capability.type='Label'" + //
                "WITH capability, type ORDER BY capability.value, type.fqn " + //
                "RETURN capability.value as capabilityValue, collect(type.fqn) as types");
        Map<String, List<String>> capabilitiesPerFile = testResult.getRows()
                .stream()
                .collect(toMap(row -> (String) row.get("capabilityValue"), row -> (List<String>) row.get("types")));
        assertThat(capabilitiesPerFile).containsExactlyInAnyOrderEntriesOf(ofEntries( //
                entry("TestLanguage", List.of(TestLanguage.class.getName(), TestType.class.getName())), //
                entry("TestType", List.of(TestType.class.getName()))));
        store.commitTransaction();
    }

    @Test
    void pluginProvidesRule() throws RuleException {
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");
        Result<Concept> result = applyConcept("jqassistant-dashboard-jqassistant-plugins:PluginProvidesRule");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(4);
        store.commitTransaction();
    }

    @Test
    void ruleRequiresConcept() throws RuleException {
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");
        Result<Concept> result = applyConcept("jqassistant-dashboard-jqassistant-plugins:RuleRequiresConcept");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(1);
        store.commitTransaction();
    }

    @Test
    void groupIncludesRule() throws RuleException {
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");
        Result<Concept> result = applyConcept("jqassistant-dashboard-jqassistant-plugins:GroupIncludesRule");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        List<Row> rows = result.getRows();
        assertThat(rows).hasSize(3);

        List<Capability> capabilities = getCapabilities(rows);

        assertThat(capabilities).haveExactly(1, capability("Concept", "test:Concept"))
                .haveExactly(1, capability("Constraint", "test:Constraint"))
                .haveExactly(1, capability("Group", "test:Group"));
        store.commitTransaction();
    }

    @Test
    void componentVersionProvidesCapability() throws RuleException {
        scanClasses(ARTIFACT_ID, TestLanguage.class, TestType.class);
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");

        Result<Concept> result = applyConcept("jqassistant-dashboard:ComponentVersionProvidesCapability");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(6);
        store.commitTransaction();
    }

    @Test
    void componentVersionRequiresCapability() throws RuleException {
        scanClasses(ARTIFACT_ID, TestLanguage.class, TestType.class);
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");

        Result<Concept> result = applyConcept("jqassistant-dashboard:ComponentVersionRequiresCapability");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(3);
        assertThat(getCapabilities(result.getRows())).haveExactly(1, capability("Concept", "test:Concept"));
        assertThat(getCapabilities(result.getRows())).haveExactly(1, capability("Constraint", "test:Constraint"));
        assertThat(getCapabilities(result.getRows())).haveExactly(1, capability("Group", "test:Group"));
        store.commitTransaction();
    }

    @Test
    void componentVersionReport() throws RuleException {
        scanClasses(ARTIFACT_ID, TestLanguage.class, TestType.class);
        scanClassPathResources(DefaultScope.NONE, ARTIFACT_ID, "/META-INF/jqassistant-rules/test.xml");

        Result<Concept> result = applyConcept("jqassistant-dashboard:ComponentVersionReport");

        store.beginTransaction();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getRows()).hasSize(1);
        store.commitTransaction();
    }

    private static List<Capability> getCapabilities(List<Row> rows) {
        return getColumn(rows, "Capability", Capability.class);
    }

    private static <T> List<T> getColumn(List<Row> rows, String columnName, Class<T> type) {
        return rows.stream()
                .map(row -> row.getColumns()
                        .get(columnName))
                .map(column -> column != null ? type.cast(column.getValue()) : null)
                .collect(toList());
    }

}
