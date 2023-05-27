package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.querymodel.DoctorDay;
import serviceregistration.dto.querymodel.DoctorSchedule;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.service.CabinetService;
import serviceregistration.service.DayService;
import serviceregistration.service.DoctorService;
import serviceregistration.service.DoctorSlotService;

import java.util.List;

@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;
    private final CabinetService cabinetService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   CabinetService cabinetService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.cabinetService = cabinetService;
    }

    @GetMapping("")
    public String getSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "8") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorSlotDTO> doctorSlots = doctorSlotService.getAllDoctorSlot(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/schedule";
    }

    @GetMapping("/doctor-schedule/{doctorId}")
    public String doctorSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "8") int pageSize,
                              @PathVariable Long doctorId,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorSchedule> doctorSchedules = doctorSlotService.getScheduleByDoctor(pageRequest, doctorId);
        model.addAttribute("doctorSchedules", doctorSchedules);
        return "doctorslots/doctorSchedule";
    }

    @GetMapping("/addSchedule")
    public String addSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        List<Cabinet> cabinets = cabinetService.listAll();
        model.addAttribute("scheduleForm", new DoctorSlotDTO());
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        model.addAttribute("cabinets", cabinets);
        return "doctorslots/addSchedule";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO,
                              BindingResult bindingResult,
                              Model model) {
        addSchedule(model);
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("day", "error.day", "Врач уже работает " + doctorSlotDTO.getDay().getDay());
            return "doctorslots/addSchedule";
        }
        if (doctorSlotService.getDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("cabinet", "error.cabinet", "В этот день кабинет занят");
            return "doctorslots/addSchedule";
        }
        doctorSlotService.addSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots";
    }

    @GetMapping("/deleteSchedule")
    public String deleteSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Day> days = dayService.listAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", days);
        return "doctorslots/deleteSchedule";
    }

    @PostMapping("/deleteSchedule")
    public String deleteSchedule(@ModelAttribute("scheduleForm") DoctorSlotDTO doctorSlotDTO) {
        doctorSlotService.deleteSchedule(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId());
        return "redirect:/doctorslots";
    }


    @GetMapping("/makeMeet")
    public String groupSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "8") int pageSize,
                             Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorDay> doctorDays = doctorSlotService.groupByDoctorSlot(pageRequest);
        model.addAttribute("doctorDays", doctorDays);
        return "/doctorslots/makeMeet";
    }

//    @GetMapping("")
//    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
//                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
//                         Model model) {
//        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
//        Page<DoctorDTO> doctors = doctorService.listAll(pageRequest);
//        List<Specialization> specializations = specializationService.listAll();
//        model.addAttribute("specializations", specializations);
//        model.addAttribute("doctors", doctors);
//        return "doctors/list";
//    }

}
