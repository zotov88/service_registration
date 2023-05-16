package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.service.DoctorSlotService;

import java.util.List;

@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService) {
        this.doctorSlotService = doctorSlotService;
    }

    @GetMapping("")
    public String getSchedule(Model model) {
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAll();
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

    @GetMapping ("/addSchedule")
    public String addSchedule() {
        return "doctorslots/addSchedule";
    }

//    @PostMapping("/addSchedule")
//    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
//                              @ModelAttribute"") {
//        doctorSlotService.getSchedule(doctorSlotDTO.getId(), );
//        return "redirect:/doctorslots";
//    }
}
