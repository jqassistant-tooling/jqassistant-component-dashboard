package org.jqassistant.tooling.dashboard.plugin.api.model;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;

@Label
public interface Contributor {

    String getName();
    void setName(String name);

    String getEmail();
    void setEmail(String email);

    String getIdentString();
    void setIdentString(String identString);

    @Relation("CONTRIBUTED")
    Set<Contributions> getContributed();
}
