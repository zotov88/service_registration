package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.dto.querymodel.ClientRegistration;
import serviceregistration.dto.querymodel.SlotRegistered;
import serviceregistration.service.*;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.List;

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

    public RegistrationMVCController(RegistrationService registrationService,
                                     DoctorSlotService doctorSlotService,
                                     DoctorService doctorService,
                                     ClientService clientService,
                                     SpecializationService specializationService,
                                     DayService dayService) {
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.specializationService = specializationService;
        this.dayService = dayService;
    }

    @GetMapping("/listAll")
    public String listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "5") int pageSize,
                          Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<RegistrationDTO> registrations = registrationService.listAll(pageRequest);
        model.addAttribute("registrations", registrations);
        return "registrations/listAll";
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
    public String clientSlots(@PathVariable Long clientId,
                              Model model) {
        List<ClientRegistration> registrationsClients = registrationService.getAllRegistrationsByClient(clientId);
        model.addAttribute("registrationsClients", registrationsClients);
        return "registrations/clientList";
    }

    @GetMapping("/client-slots/cancel/{registrationId}")
    public String cancelMeet(@PathVariable Long registrationId) {
        registrationService.cancelMeet(registrationId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "redirect:/registrations/client-slots/" + customUserDetails.getUserId();
    }

    @GetMapping("/client-slots/cancel/ds/{doctorSlotId}")
    public String cancelMeetByDs(@PathVariable Long doctorSlotId) {
        Long registrationId = registrationService.getIdByDoctorSlotId(doctorSlotId);
        registrationService.cancelMeet(registrationId);
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(doctorSlotId);
        return "redirect:/doctorslots/doctor-schedule/day/" + doctorSlotDTO.getDoctor().getId() + "/" + doctorSlotDTO.getDay().getId();
    }

    @GetMapping("/doctor-slots-today/{doctorId}")
    public String doctorSlots(@PathVariable Long doctorId,
                              Model model) {
        List<SlotRegistered> slotRegistereds = doctorSlotService.getScheduleByDoctorToday(doctorId);
        model.addAttribute("slotRegistered", slotRegistereds);
        return "registrations/doctorList";
    }

    @GetMapping("/slots/{doctorId}/{dayId}")
    public String createMeet(@PathVariable Long doctorId,
                             @PathVariable Long dayId,
                             Model model) {
        model.addAttribute("timeSlots", doctorSlotService.getSlotsForDoctorDay(doctorId, dayId));
        model.addAttribute("doctor", doctorService.getOne(doctorId));
        model.addAttribute("day", dayService.getOne(dayId));
        model.addAttribute("specialization", specializationService.listAll());
        model.addAttribute("cabinet", doctorSlotService.getCabinetByDoctorIdAndDayId(doctorId, dayId));
        return "registrations/chooseTime";
    }

    @GetMapping("/slots/create/{doctorSlotId}")
    public String createMeet(@PathVariable Long doctorSlotId,
                             @ModelAttribute("registrationSlot") RegistrationDTO registrationDTO) {
        DoctorSlotDTO doctorSlot = doctorSlotService.getOne(doctorSlotId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDTO clientDTO = clientService.getClientByLogin(customUserDetails.getUsername());
        if (clientService.isActiveRegistrationBySpecialization(doctorSlot, clientDTO.getId())) {
            return "redirect:/doctorslots/makeMeet";
        }
        if (clientService.isActiveRegistrationByDayAndTime(doctorSlot, clientDTO.getId())) {
            return "redirect:/doctorslots/makeMeet";
        }
        registrationDTO.setClientId(Long.valueOf(customUserDetails.getUserId()));
        registrationDTO.setDoctorSlotId(doctorSlot.getId());
        registrationService.registrationSlot(registrationDTO);
        return "redirect:/registrations/client-slots/" + customUserDetails.getUserId();
    }




//    @GetMapping("/restore/{doctorId}/{dayId}")
//    public String restore(@PathVariable Long doctorId,
//                          @PathVariable Long dayId) {
//        doctorSlotService.restore(doctorId, dayId);
//        return "redirect:/doctorslots";
//    }

}
