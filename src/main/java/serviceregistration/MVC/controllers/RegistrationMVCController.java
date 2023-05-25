package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import serviceregistration.customcomponent.DoctorDay;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Day;
import serviceregistration.service.DayService;
import serviceregistration.service.DoctorService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;

import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;

    public RegistrationMVCController(RegistrationService registrationService,
                                     DoctorSlotService doctorSlotService,
                                     DoctorService doctorService,
                                     DayService dayService) {
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;

        this.dayService = dayService;
    }

    @GetMapping("/listAll")
    public String listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "1") int pageSize,
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

    @GetMapping("/makeMeet")
    public String createMeet(Model model) {
        List<DoctorDay> doctorDays = doctorSlotService.groupByDoctorSlot();
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        model.addAttribute("doctorDays", doctorDays);
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        return "/registrations/makeMeet";
    }

    @GetMapping("/client-slots/{id}")
    public String clientSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "2") int pageSize,
                            @PathVariable Long id,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<RegistrationDTO> registrationDTOS = registrationService.getAllByClientId(pageRequest, id);
        model.addAttribute("registratedSlot", registrationDTOS);
        return "registrations/clientList";
    }

}
