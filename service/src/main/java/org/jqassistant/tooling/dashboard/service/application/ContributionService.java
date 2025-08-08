package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Contributions;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;

    public Contributions resolve(String ident, String componentID){
        return contributionRepository.resolveContribution(ident, componentID);
    }
}
