package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Slot;
import serviceregistration.service.*;

import java.util.List;

@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;
    private final SlotService slotService;
    private final CabinetService cabinetService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   SlotService slotService,
                                   CabinetService cabinetService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.slotService = slotService;
        this.cabinetService = cabinetService;
    }

    @GetMapping("")
    public String getSchedule(Model model) {
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAll();
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

    @GetMapping ("/addSchedule")
    public String addSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        List<Slot> slots = slotService.listAll();
        List<Cabinet> cabinets = cabinetService.listAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        model.addAttribute("slots", slots);
        model.addAttribute("cabinets", cabinets);
        return "doctorslots/addSchedule";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
                              BindingResult bindingResult) {
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("day", "error.day", "Врач уже работает " + doctorSlotDTO.getDay().getDay());
            return "doctorslots/addSchedule";
        }
        if (doctorSlotService.getDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("cabinet", "error.cabinet", "В этот день кабинет занят");
            return "doctorslots/addSchedule";
        }
        doctorSlotService.getSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots";
    }
}
