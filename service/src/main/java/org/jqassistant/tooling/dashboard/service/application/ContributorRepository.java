package org.jqassistant.tooling.dashboard.service.application;

import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository {

    Contributor resolveContributor(String identString);

}
