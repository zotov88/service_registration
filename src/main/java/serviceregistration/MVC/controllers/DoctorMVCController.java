package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;
import serviceregistration.service.SpecializationService;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorMVCController {

    private final DoctorService doctorService;
    private final SpecializationService specializationService;

    public DoctorMVCController(DoctorService doctorService,
                               SpecializationService specializationService) {
        this.doctorService = doctorService;
        this.specializationService = specializationService;
    }

    @GetMapping("")
    public String getAll(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        model.addAttribute("doctors", doctors);
        return "doctors/all";
    }

    @GetMapping("/add")
    public String add(Model model) {
        List<Specialization> specializations = specializationService.listAll();
        model.addAttribute("specializations", specializations);
        return "doctors/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("doctorForm") DoctorDTO doctorDTO) {
        doctorService.create(doctorDTO);
        return "redirect:/doctors";
    }
}
