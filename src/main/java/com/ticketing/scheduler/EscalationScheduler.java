package com.ticketing.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// TODO: implemented in Phase X
@Component
public class EscalationScheduler {

    @Scheduled(cron = "${app.escalation.scheduler-cron:0 */30 * * * *}")
    public void runEscalationCheck() {
        // TODO: implemented in Phase X
    }
}
