package org.jqassistant.tooling.dashboard.service.application;

import java.util.List;
import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.jqassistant.tooling.dashboard.service.application.model.Version;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository {

    Component resolve(String project, String component);

    Stream<ComponentSummary> findAll(Project project, List<String> nameFilter, List<String> descriptionFilter, int offset, int limit);

    int countAll(Project project, List<String> nameFilter, List<String> descriptionFilter);

    ComponentSummary find(Project project, String componentId);

    interface ComponentSummary {

        Component getComponent();

        Version getLatestVersion();

        Long getVersionCount();
    }
}
