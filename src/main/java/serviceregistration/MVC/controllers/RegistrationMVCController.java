package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.service.RegistrationService;

import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationMVCController {

    private final RegistrationService registrationService;

    public RegistrationMVCController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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
}
