package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.constants.Days;
import serviceregistration.dto.*;
import serviceregistration.model.Cabinet;
import serviceregistration.querymodel.UniversalQueryModel;
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
    private final DoctorSlotRegistrationService doctorSlotRegistrationService;
    private final ClientService clientService;
    private final RegistrationService registrationService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   CabinetService cabinetService,
                                   SpecializationService specializationService,
                                   DoctorSlotRegistrationService doctorSlotRegistrationService,
                                   ClientService clientService,
                                   RegistrationService registrationService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.cabinetService = cabinetService;
        this.specializationService = specializationService;
        this.doctorSlotRegistrationService = doctorSlotRegistrationService;
        this.clientService = clientService;
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public String getActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                    Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getActualSchedule(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getFirstFiveActualDays(Days.ONE_WEEK));
        model.addAttribute("cabinets", cabinetService.listAll());
        return "doctorslots/schedule";
    }

    @PostMapping("/search")
    public String searchActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                       @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                       Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctorslots", doctorSlotService.findAmongActualSchedule(pageRequest, doctorSlotSearchDTO));
        model.addAttribute("days", dayService.getFirstFiveActualDays(Days.ONE_WEEK));
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        return "doctorslots/schedule";
    }

    @GetMapping("/archive")
    public String getArchiveSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                     Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getArchiveSchedule(pageRequest);
        model.addAttribute("days", dayService.listAll());
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("doctorslots", doctorSlots);
        return "doctorslots/archiveSchedule";
    }

    @PostMapping("/archive/search")
    public String searchArchiveSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                        @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                        Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctorslots", doctorSlotService.findAmongAllSchedule(pageRequest, doctorSlotSearchDTO));
        model.addAttribute("days", dayService.listAll());
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        return "doctorslots/archiveSchedule";
    }

    @GetMapping("/doctor-schedule/{doctorId}")
    public String doctorSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "8") int pageSize,
                              @PathVariable Long doctorId,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSchedules = doctorSlotService.getScheduleByDoctor(pageRequest, doctorId);
        model.addAttribute("doctorSchedules", doctorSchedules);
        return "doctorslots/doctorSchedule";
    }

    @GetMapping("/doctor-schedule/day/{doctorId}/{dayId}")
    public String getSlotsForDoctorDayByLogin(@PathVariable Long doctorId,
                                              @PathVariable Long dayId,
                                              Model model) {
        model.addAttribute("timeSlots", doctorSlotService.getSlotsOneDayForDoctor(doctorId, dayId));
        model.addAttribute("doctor", doctorService.getOne(doctorId));
        model.addAttribute("day", dayService.getOne(dayId));
        model.addAttribute("cabinet", doctorSlotService.getCabinetByDoctorIdAndDayId(doctorId, dayId));
        model.addAttribute("canCancelIds", doctorSlotService.getPosiblyCancelMeet(doctorId, dayId));
        return "doctorslots/scheduleDay";
    }

    @GetMapping("/doctor-schedule/day/slot/{doctorSlotId}")
    public String getInfoAboutClient(@PathVariable Long doctorSlotId,
                                     Model model) {
        ClientDTO clientDTO = clientService.getClientIdByDoctorSlot(doctorSlotId);
        model.addAttribute("clientDTO", clientDTO);
//        model.addAttribute("doctorSlot", doctorSlotService.getOne(doctorSlotId));
        return "clients/viewClient";
    }

    @GetMapping("/addSchedule")
    public String addSchedule(Model model) {
        List<DoctorDTO> doctors = doctorService.listAll();
        List<Cabinet> cabinets = cabinetService.listAll();
        model.addAttribute("scheduleForm", new DoctorDayCabinetDTO());
        model.addAttribute("doctors", doctors);
        model.addAttribute("days", dayService.getFirstFiveActualDays(Days.ONE_WEEK));
        model.addAttribute("cabinets", cabinets);
        return "doctorslots/addSchedule";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorDayCabinetDTO doctorDayCabinetDTO,
                              BindingResult bindingResult,
                              Model model) {
        addSchedule(model);
        DoctorSlotDTO doctorSlotDTO = new DoctorSlotDTO();
        doctorSlotDTO.setDoctor(doctorDayCabinetDTO.getDoctor());
        doctorSlotDTO.setCabinet(doctorDayCabinetDTO.getCabinet());
        doctorSlotDTO.setDay(dayService.getDayByDate(doctorDayCabinetDTO.getDay().toString()));
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

    @GetMapping("/delete/{doctorId}/{dayId}")
    public String deleteSoft(@PathVariable Long doctorId,
                             @PathVariable Long dayId) {
        doctorSlotRegistrationService.deleteSoft(doctorId, dayId);
        return "redirect:/doctorslots";
    }

    @GetMapping("/restore/{doctorId}/{dayId}")
    public String restore(@PathVariable Long doctorId,
                          @PathVariable Long dayId) {
        doctorSlotService.restore(doctorId, dayId);
        return "redirect:/doctorslots";
    }

    @GetMapping("/makeMeet")
    public String groupSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "8") int pageSize,
                             Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorDays = doctorSlotService.groupByDoctorSlot(pageRequest);
        model.addAttribute("doctorDays", doctorDays);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getActualDays());
        return "/doctorslots/makeMeet";
    }

    @PostMapping("/makeMeet/search")
    public String searchGroupSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                   @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                   Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorDays = doctorSlotService.findAmongGroupByDoctorSlot(pageRequest, doctorSlotSearchDTO);
        model.addAttribute("doctorDays", doctorDays);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getActualDays());
        return "doctorslots/makeMeet";
    }

    @GetMapping("/schedule/actual/{doctorId}")
    public String filterDoctorSchedule(@PathVariable Long doctorId,
                                       Model model) {
        DoctorDTO doctorDTO = doctorService.getOne(doctorId);
        DoctorSlotSearchDTO doctorSlotSearchDTO = new DoctorSlotSearchDTO();
        doctorSlotSearchDTO.setFirstName(doctorDTO.getFirstName());
        doctorSlotSearchDTO.setMidName(doctorDTO.getMidName());
        doctorSlotSearchDTO.setLastName(doctorDTO.getLastName());
        doctorSlotSearchDTO.setSpecialization(doctorDTO.getSpecialization().getTitleSpecialization());
        return searchGroupSlots(1, 12, doctorSlotSearchDTO, model);

    }

}
