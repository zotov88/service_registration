package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.RegistrationService;
import serviceregistration.service.SpecializationService;

import java.util.List;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/doctors")
public class DoctorMVCController {

    private final DoctorService doctorService;
    private final DoctorSlotService doctorSlotService;
    private final SpecializationService specializationService;
    private final RegistrationService registrationService;

    public DoctorMVCController(DoctorService doctorService,
                               DoctorSlotService doctorSlotService,
                               SpecializationService specializationService,
                               RegistrationService registrationService) {
        this.doctorService = doctorService;
        this.doctorSlotService = doctorSlotService;
        this.specializationService = specializationService;
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorDTO> doctors = doctorService.listAllSort(pageRequest);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("doctors", doctors);
        return "doctors/list";
    }

    @PostMapping("/search")
    public String searchDoctor(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int pageSize,
                               @ModelAttribute("doctorSearchForm") DoctorSearchDTO doctorSearchDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctors", doctorService.findDoctors(doctorSearchDTO, pageRequest));
        model.addAttribute("specializations", specializationService.listAll());
        return "doctors/list";
    }

    @GetMapping("/{id}")
    public String viewDoctor(@PathVariable Long id,
                             Model model) {
        model.addAttribute("doctor", doctorService.getOne(id));
        return "doctors/viewDoctor";
    }

    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        List<Specialization> specializations = specializationService.listAll();
        model.addAttribute("specializations", specializations);
        model.addAttribute("doctorForm", new DoctorDTO());
        return "doctors/addDoctor";
    }

    @PostMapping("/addDoctor")
    public String addDoctor(@ModelAttribute("doctorForm") DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model) {
        addDoctor(model);
        String login = doctorDTO.getLogin().toLowerCase();
        doctorDTO.setLogin(login);
        if (doctorDTO.getLogin().equalsIgnoreCase(ADMIN) ||
                doctorService.getDoctorByLogin(login) != null && doctorService.getDoctorByLogin(login).getLogin().equals(login)) {
            bindingResult.rejectValue("login", "error.login", "Этот логин уже существует");
            return "doctors/addDoctor";
        }
        doctorService.create(doctorDTO);
        return "redirect:/doctors";
    }

    @GetMapping("/softdelete/{doctorId}")
    public String softDelete(@PathVariable Long doctorId) {
        doctorService.softDelete(doctorId);
        return "redirect:/doctors";
    }

    @GetMapping("/restore/{doctorId}")
    public String restore(@PathVariable Long doctorId) {
        doctorService.restore(doctorId);
        return "redirect:/doctors";
    }

    @GetMapping("/deleteDoctor")
    public String deleteDoctor(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        model.addAttribute("doctors", doctors);
        return "doctors/deleteDoctor";
    }

    @PostMapping("/deleteDoctor")
    public String deleteDoctor(@RequestParam("doctorDel") Long doctorId) {
        doctorService.delete(doctorId);
        return "redirect:/doctors";
    }
}
