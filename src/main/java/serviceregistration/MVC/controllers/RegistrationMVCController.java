package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.customcomponent.DoctorDay;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Day;
import serviceregistration.model.Specialization;
import serviceregistration.service.*;

import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;
    private final DayService dayService;

    public RegistrationMVCController(RegistrationService registrationService,
                                     DoctorSlotService doctorSlotService,
                                     DoctorService doctorService,
                                     SpecializationService specializationService,
                                     DayService dayService) {
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.dayService = dayService;
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        List<RegistrationDTO> registrations = registrationService.listAll();
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
        List<Specialization> specializations = specializationService.listAll();
        List<Day> days = dayService.listAll();
        model.addAttribute("doctorDays", doctorDays);
        model.addAttribute("doctors", doctors);
        model.addAttribute("specializations", specializations);
        model.addAttribute("days", days);
        return "/registrations/makeMeet";
    }


}
