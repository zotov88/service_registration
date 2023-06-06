package serviceregistration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serviceregistration.service.RegistrationService;

@Slf4j
@Component
public class Scheduler {

    private final RegistrationService registrationService;

    public Scheduler(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Scheduled(cron = "0 0/30 7-21 * * *")
    public void setPastRegistrationsToFalse() {
        registrationService.setCompletedMeetingToFalse();
    }

}
