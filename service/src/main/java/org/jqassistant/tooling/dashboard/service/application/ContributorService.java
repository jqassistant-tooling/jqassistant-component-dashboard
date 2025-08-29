package org.jqassistant.tooling.dashboard.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jqassistant.tooling.dashboard.service.application.model.Contributor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributorService {

    private final ContributorRepository contributorRepository;

    public Contributor resolve(String identString) {
        return contributorRepository.resolveContributor(identString);
    }

}
