package org.jqassistant.tooling.dashboard.service.adapters.secondary.xo;

import com.buschmais.xo.api.XOManager;
import org.jqassistant.tooling.dashboard.service.application.ComponentRepository;
import org.jqassistant.tooling.dashboard.service.application.model.Component;
import org.jqassistant.tooling.dashboard.service.application.model.Project;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class ComponentRepositoryImpl extends AbstractXORepository<XOComponentRepository> implements ComponentRepository {

    ComponentRepositoryImpl(XOManager xoManager) {
        super(xoManager, XOComponentRepository.class);
    }

    @Override
    public Component resolve(String project, String component) {
        return getXORepository().resolve(project, component);
    }

    @Override
    public Stream<ComponentSummary> findAll(Project project, List<String> nameFilter, List<String> descriptionFilter, int offset, int limit) {
        return toStream(xoManager.createQuery(XOComponentRepository.XOComponentSummary.class)
            .withParameter("project", project)
            .withParameter("nameFilter", nameFilter)
            .withParameter("descriptionFilter", descriptionFilter)
            .withParameter("offset", offset)
            .withParameter("limit", limit)
            .execute()).map(componentSummary -> componentSummary);
    }

    @Override
    public int countAll(Project project, List<String> nameFilter, List<String> descriptionFilter) {
        return getXORepository().countAll(project, nameFilter, descriptionFilter);
    }

}
