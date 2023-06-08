package serviceregistration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import serviceregistration.constants.MailConstants;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.service.MailSenderService;
import serviceregistration.service.RegistrationService;

import java.util.List;

@Slf4j
@Component
public class Scheduler {

    private final RegistrationService registrationService;
    private final MailSenderService mailSenderService;

    public Scheduler(RegistrationService registrationService,
                     MailSenderService mailSenderService) {
        this.registrationService = registrationService;
        this.mailSenderService = mailSenderService;
    }

    @Scheduled(cron = "0 0/30 7-21 * * *")
    public void setPastRegistrationsToFalse() {
        registrationService.setCompletedMeetingToFalse();
    }

    @Scheduled(cron = "0 0/30 7-21 * * *")
    public void notifyDayBeforeMeeting() {
        List<RegistrationDTO> registrationDTOList = registrationService.getMeetingsBeforeDay();
        for (RegistrationDTO registrationDTO : registrationDTOList) {
            mailSenderService.sendMessage(
                    registrationDTO.getId(),
                    MailConstants.MAIL_SUBJECT_FOR_NOTIFY,
                    MailConstants.MAIL_BODY_FOR_NOTIFY);
        }
    }

}
