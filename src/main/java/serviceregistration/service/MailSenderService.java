package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.utils.MailUtils;

import java.util.Formatter;

@Slf4j
@Service
public class MailSenderService {

    private final JavaMailSender javaMailSender;
    private final RegistrationService registrationService;
    private final ClientService clientService;
    private final DoctorService doctorService;
    private final DoctorSlotService doctorSlotService;
    private final DayService dayService;
    private final SlotService slotService;
    private final CabinetService cabinetService;

    public MailSenderService(JavaMailSender javaMailSender,
                             RegistrationService registrationService,
                             ClientService clientService,
                             DoctorService doctorService,
                             DoctorSlotService doctorSlotService,
                             DayService dayService,
                             SlotService slotService,
                             CabinetService cabinetService) {
        this.javaMailSender = javaMailSender;
        this.registrationService = registrationService;
        this.clientService = clientService;
        this.doctorService = doctorService;
        this.doctorSlotService = doctorSlotService;
        this.dayService = dayService;
        this.slotService = slotService;
        this.cabinetService = cabinetService;
    }

    public void sendMessage(final Long registrationId,
                            final String subject,
                            final String text) {
        RegistrationDTO registrationDTO = registrationService.getOne(registrationId);
        ClientDTO clientDTO = clientService.getOne(registrationDTO.getClientId());
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(registrationDTO.getDoctorSlotId());
        DoctorDTO doctorDTO = doctorService.getOne(doctorSlotDTO.getDoctor().getId());
        Day day = dayService.getOne(doctorSlotDTO.getDay().getId());
        Slot slot = slotService.getOne(doctorSlotDTO.getSlot().getId());
        Cabinet cabinet = cabinetService.getOne(doctorSlotDTO.getCabinet().getId());
        sendMessageRegistrationStatus(clientDTO, doctorDTO, day, slot, cabinet, subject, text);
    }

    public void sendMessageRegistrationStatus(final ClientDTO clientDTO,
                                              final DoctorDTO doctorDTO,
                                              final Day day,
                                              final Slot slot,
                                              final Cabinet cabinet,
                                              final String subject,
                                              final String text) {
        SimpleMailMessage mailMessage = MailUtils.createMailMessage(
                clientDTO.getEmail(),
                subject,
                createMessageRegistrationStatus(clientDTO, doctorDTO, day, slot, cabinet, text)
        );
        // Защита от отправки писем на все почты кроме этой
        if (clientDTO.getEmail().equalsIgnoreCase("zotov_l88@mail.ru")) {
            javaMailSender.send(mailMessage);
        }
    }

    private String createMessageRegistrationStatus(final ClientDTO clientDTO,
                                                   final DoctorDTO doctorDTO,
                                                   final Day day,
                                                   final Slot slot,
                                                   final Cabinet cabinet,
                                                   final String message) {
        Formatter formatter = new Formatter().format(
                        """
                        %s%s, %s.

                        Доктор: %s %s %s
                        Специализация: %s
                        Дата: %s
                        Время: %s
                        Кабинет: %d
                        """,
                clientDTO.getFirstName(), clientDTO.getMidName().isEmpty() ? "" : (" " + clientDTO.getMidName()),
                message,
                doctorDTO.getLastName(), doctorDTO.getFirstName(), doctorDTO.getMidName(),
                doctorDTO.getSpecialization().getTitleSpecialization(),
                day.getDay(), slot.getTimeSlot(), cabinet.getCabinetNumber());
        return formatter.toString();
    }
}
