package serviceregistration.MVC.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.querymodel.ClientRegistration;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.dto.querymodel.DoctorRegistration;
import serviceregistration.service.*;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;

    public RegistrationMVCController(RegistrationService registrationService,
                                     DoctorSlotService doctorSlotService) {
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
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

    @GetMapping("/doctor-slots/{doctorId}")
    public String doctorSlots(@PathVariable Long doctorId,
                              Model model) {
        List<DoctorRegistration> doctorRegistrations = registrationService.getAllRegistrationsByDoctor(doctorId);
        model.addAttribute("doctorRegistrations", doctorRegistrations);
        return "registrations/doctorList";
    }

    @GetMapping("/slots/{doctorId}/{dayId}")
    public String createMeet(@PathVariable Long doctorId,
                             @PathVariable Long dayId,
                             Model model) {
        model.addAttribute("timeSlots", doctorSlotService.getDoctorSlotsIdsAndTimeSlotsFree(doctorId, dayId));
        return "registrations/chooseTime";
    }

    @PostMapping("/slots")
    public String createMeet(@RequestParam("timeSlot") Long doctorSlotId,
                             @ModelAttribute("registrationSlot") RegistrationDTO registrationDTO) {
        DoctorSlotDTO doctorSlot = doctorSlotService.getOne(doctorSlotId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        registrationDTO.setClientId(Long.valueOf(customUserDetails.getUserId()));
        registrationDTO.setDoctorSlotId(doctorSlot.getId());
        registrationService.registrationSlot(registrationDTO);
        return "redirect:/registrations/client-slots/" + customUserDetails.getUserId();
    }

}
