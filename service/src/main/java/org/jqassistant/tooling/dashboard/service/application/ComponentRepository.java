package org.jqassistant.tooling.dashboard.service.application;

import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository {

    Component resolve(String projectId, String componentId);

    Stream<Component> findAll(Project project, String nameFilter, int offset, int limit);

    int countAll(Project project, String nameFilter);

}
