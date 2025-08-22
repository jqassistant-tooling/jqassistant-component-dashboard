package org.jqassistant.tooling.dashboard.service;

import org.jqassistant.tooling.dashboard.service.application.model.*;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class ModelFixture {

    public static Component stubComponent(String name) {
        Component component = mock(Component.class);
        lenient().doReturn(name)
            .when(component)
            .getName();
        return component;
    }

    public static Project stubProject(String name) {
        Project project = mock(Project.class);
        lenient().doReturn(name)
            .when(project)
            .getName();
        return project;
    }

    public static Version stubVersion(String versionId) {
        Version version = mock(Version.class);
        lenient().doReturn(versionId)
            .when(version)
            .getVersion();
        return version;
    }

    public static Contributor stubContributor(String identString) {
        Contributor contributor = mock(Contributor.class);
        lenient().doReturn(identString)
            .when(contributor)
            .getIdentString();
        return contributor;
    }

    public static Contribution stubContribution() {
        return mock(Contribution.class);
    }

}
