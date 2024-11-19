package org.jqassistant.tooling.dashboard.service.application;

import java.util.stream.Stream;

import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository {

    Component resolve(String projectId, String componentId);

    Stream<Component> findAll(String nameFilter, int offset, int limit);

    int countAll(String nameFilter);

}
