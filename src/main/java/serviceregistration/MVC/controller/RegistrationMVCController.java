package serviceregistration.MVC.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.service.*;
import serviceregistration.service.userdetails.CustomUserDetails;

import static serviceregistration.constants.MailConstants.*;

@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final ClientService clientService;
    private final SpecializationService specializationService;
    private final DayService dayService;
    private final MailSenderService mailSenderService;

    public RegistrationMVCController(RegistrationService registrationService,
                                     DoctorSlotService doctorSlotService,
                                     DoctorService doctorService,
                                     ClientService clientService,
                                     SpecializationService specializationService,
                                     DayService dayService,
                                     MailSenderService mailSenderService) {
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.specializationService = specializationService;
        this.dayService = dayService;
        this.mailSenderService = mailSenderService;
    }

    @GetMapping("/doctorList")
    public String doctorList() {
        return "registrations/doctorList";
    }

    @GetMapping("/clientList")
    public String clientList() {
        return "registrations/clientList";
    }

    @GetMapping("/client-slots/{clientId}")
    public String clientSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "10") int pageSize,
                              @PathVariable Long clientId,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("registrationsClients", registrationService.getAllRegistrationsByClient(clientId, pageRequest));
        model.addAttribute("client", clientService.getOne(clientId));
        return "registrations/clientList";
    }

    @GetMapping("/slots/{doctorId}/{dayId}")
    public String createMeet(@PathVariable Long doctorId,
                             @PathVariable Long dayId,
                             Model model) {
        model.addAttribute("timeSlots", doctorSlotService.getSlotsOneDayForClient(doctorId, dayId));
        model.addAttribute("doctor", doctorService.getOne(doctorId));
        model.addAttribute("day", dayService.getOne(dayId));
        model.addAttribute("specialization", specializationService.listAll());
        model.addAttribute("cabinet", doctorSlotService.getCabinetByDoctorIdAndDayId(doctorId, dayId));
        return "registrations/chooseTime";
    }

    @GetMapping("/slots/create/{doctorSlotId}")
    public String createMeet(@PathVariable Long doctorSlotId,
                             @ModelAttribute("registrationSlot") RegistrationDTO registrationDTO) {
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(doctorSlotId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDTO clientDTO = clientService.getClientByLogin(customUserDetails.getUsername());
        if (clientService.isActiveRegistrationBySpecialization(doctorSlotDTO, clientDTO.getId())) {
            return "redirect:/doctorslots/makeMeet";
        }
        if (clientService.isActiveRegistrationByDayAndTime(doctorSlotDTO, clientDTO.getId())) {
            return "redirect:/doctorslots/makeMeet";
        }
        registrationDTO.setClientId(Long.valueOf(customUserDetails.getUserId()));
        registrationDTO.setDoctorSlotId(doctorSlotDTO.getId());
        registrationService.registrationSlot(registrationDTO);
        mailSenderService.sendMessage(
                registrationService.getRegistrationDTOByDoctorSlotId(doctorSlotDTO.getId()).getId(),
                MAIL_SUBJECT_FOR_REGISTRATION_SUCCESS,
                MAIL_BODY_FOR_REGISTRATION_SUCCESS);
        return "redirect:/registrations/client-slots/" + customUserDetails.getUserId();
    }

    @GetMapping("/client-slots/cancel/{registrationId}")
    public String cancelMeet(@PathVariable Long registrationId) {
        ClientDTO clientDTO = clientService.getOne(registrationService.getOne(registrationId).getClientId());
        registrationService.cancelMeet(registrationId);
        mailSenderService.sendMessage(
                registrationId,
                MAIL_SUBJECT_FOR_REGISTRATION_CANCEL,
                MAIL_BODY_FOR_REGISTRATION_CANCEL);
        return "redirect:/registrations/client-slots/" + clientDTO.getId();
    }

    @GetMapping("/client-slots/cancel/ds/{doctorSlotId}")
    public String cancelMeetByDs(@PathVariable Long doctorSlotId) {
        RegistrationDTO registrationDTO = registrationService.getRegistrationDTOByDoctorSlotId(doctorSlotId);
        registrationService.cancelMeet(registrationDTO.getId());
        mailSenderService.sendMessage(
                registrationDTO.getId(), MAIL_SUBJECT_FOR_REGISTRATION_CANCEL, MAIL_BODY_FOR_REGISTRATION_CANCEL);
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(doctorSlotId);
        return "redirect:/doctorslots/doctor-schedule/day/" + doctorSlotDTO.getDoctor().getId() + "/" + doctorSlotDTO.getDay().getId();
    }

    @GetMapping("/doctor-slots-today/{doctorId}")
    public String doctorSlots(@PathVariable Long doctorId,
                              Model model) {
        return "redirect:/doctorslots/doctor-schedule/day/" + doctorId + "/" + dayService.getTodayId();
    }

}
