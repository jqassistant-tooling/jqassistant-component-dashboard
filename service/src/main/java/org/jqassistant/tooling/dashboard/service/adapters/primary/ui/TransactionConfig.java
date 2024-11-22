package org.jqassistant.tooling.dashboard.service.adapters.primary.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@RequiredArgsConstructor
public class TransactionConfig {

    private final PlatformTransactionManager transactionManager;

    @Bean
    TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager);
    }

}
