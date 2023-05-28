package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.dto.querymodel.DoctorDay;
import serviceregistration.dto.querymodel.DoctorSchedule;
import serviceregistration.dto.querymodel.DoctorSlotForSchedule;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.service.*;

import java.util.List;

@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;
    private final CabinetService cabinetService;
    private final SpecializationService specializationService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   CabinetService cabinetService,
                                   SpecializationService specializationService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.cabinetService = cabinetService;
        this.specializationService = specializationService;
    }

    @GetMapping("")
    public String getSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "8") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorSlotForSchedule> doctorSlots = doctorSlotService.getActualSchedule(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getActualDays());
        model.addAttribute("cabinets", cabinetService.listAll());
        return "doctorslots/schedule";
    }


    @PostMapping("/search")
    public String searchActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                       @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                       Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctorslots", doctorSlotService.findAmongActualSchedule(pageRequest, doctorSlotSearchDTO));
        model.addAttribute("days", dayService.getActualDays());
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        return "doctorslots/schedule";
    }

    @GetMapping("/archive")
    public String getArchiveSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                     Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorSlotForSchedule> doctorSlots = doctorSlotService.getArchiveSchedule(pageRequest);
        model.addAttribute("days", dayService.listAll());
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/archiveSchedule";
    }

    @PostMapping("/archive/search")
    public String searchArchiveSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                        @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                        Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctorslots", doctorSlotService.findAmongAllSchedule(pageRequest, doctorSlotSearchDTO));
        model.addAttribute("days", dayService.listAll());
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        return "doctorslots/archiveSchedule";
    }


    @GetMapping("/slots/{doctorId}/{dayId}")
    public String getSlotsForDoctorDay(@PathVariable Long doctorId,
                                       @PathVariable Long dayId,
                                       Model model) {
        model.addAttribute("timeSlots", doctorSlotService.getSlotsForDoctorDay(doctorId, dayId));
        return "doctorslots/scheduleDay";
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
