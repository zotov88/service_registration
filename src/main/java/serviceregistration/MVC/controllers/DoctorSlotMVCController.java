package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.constants.Days;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorDayCabinetDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.model.Day;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.service.*;

@Controller
@RequestMapping("/doctorslots")
public class DoctorSlotMVCController {

    private final DoctorSlotService doctorSlotService;
    private final DoctorService doctorService;
    private final DayService dayService;
    private final CabinetService cabinetService;
    private final SpecializationService specializationService;
    private final ClientService clientService;

    public DoctorSlotMVCController(DoctorSlotService doctorSlotService,
                                   DoctorService doctorService,
                                   DayService dayService,
                                   CabinetService cabinetService,
                                   SpecializationService specializationService,
                                   ClientService clientService) {
        this.doctorSlotService = doctorSlotService;
        this.doctorService = doctorService;
        this.dayService = dayService;
        this.cabinetService = cabinetService;
        this.specializationService = specializationService;
        this.clientService = clientService;
    }

    @GetMapping("")
    public String getActualSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                    Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getActualSchedule(pageRequest);
        model.addAttribute("doctorslots", doctorSlots);
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getFirstActualDays(Days.ONE_WEEK));
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
        model.addAttribute("days", dayService.getFirstActualDays(Days.ONE_WEEK));
        model.addAttribute("cabinets", cabinetService.listAll());
        model.addAttribute("specializations", specializationService.listAll());
        return "doctorslots/schedule";
    }

    @GetMapping("/archive")
    public String getArchiveSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                     Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getArchiveSchedule(pageRequest);
        model.addAttribute("days", dayService.getDaysFromStartToPlusDaysFromToday(Days.ONE_WEEK));
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

    @GetMapping("/doctor-schedule/{doctorId}")
    public String doctorSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "8") int pageSize,
                              @PathVariable Long doctorId,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getActualScheduleByDoctor(pageRequest, doctorId);
        model.addAttribute("doctorSlots", doctorSlots);
        return "doctorslots/doctorSchedule";
    }

    @GetMapping("/doctor-schedule/archive/{doctorId}")
    public String getIndividualArchiveDoctorSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                                     @PathVariable Long doctorId,
                                                     Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.getArchiveScheduleByDoctor(pageRequest, doctorId);
        model.addAttribute("days", dayService.getDaysFromStartToPlusDaysFromToday(Days.ONE_WEEK));
        model.addAttribute("doctorSlots", doctorSlots);
        return "doctorslots/individualArchiveSchedule";
    }

    @PostMapping("/doctor-schedule/archive/search/{doctorId}")
    public String searchIndividualArchiveDoctorSchedule(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "8") int pageSize,
                                                        @ModelAttribute("dayForm") Day day,
                                                        @PathVariable Long doctorId,
                                                        Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UniversalQueryModel> doctorSlots = doctorSlotService.searchArchiveScheduleByDoctor(pageRequest, doctorId, day);
        model.addAttribute("days", dayService.getDaysFromStartToPlusDaysFromToday(Days.ONE_WEEK));
        model.addAttribute("doctorSlots", doctorSlots);
        return "doctorslots/individualArchiveSchedule";
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
        model.addAttribute("clientDTO", clientService.getClientIdByDoctorSlot(doctorSlotId));
        return "clients/viewClient";
    }

    @GetMapping("/addSchedule")
    public String addSchedule(Model model) {
        model.addAttribute("scheduleForm", new DoctorDayCabinetDTO());
        createModelForAddingSchedule(model);
        return "doctorslots/addSchedule";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@ModelAttribute("scheduleForm") DoctorDayCabinetDTO doctorDayCabinetDTO,
                              BindingResult bindingResult,
                              Model model) {
        DoctorSlotDTO doctorSlotDTO = new DoctorSlotDTO();
        doctorSlotDTO.setDoctor(doctorDayCabinetDTO.getDoctor());
        doctorSlotDTO.setCabinet(doctorDayCabinetDTO.getCabinet());
        doctorSlotDTO.setDay(dayService.getDayByDate(doctorDayCabinetDTO.getDay().toString()));
        if (doctorSlotService.getDoctorSlotByDoctorAndDay(doctorSlotDTO.getDoctor().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("day", "error.day", "Врач уже работает " + doctorSlotDTO.getDay().getDay());
            createModelForAddingSchedule(model);
            return "doctorslots/addSchedule";
        }
        if (doctorSlotService.getDoctorSlotByCabinetAndDay(doctorSlotDTO.getCabinet().getId(), doctorSlotDTO.getDay().getId()) != null) {
            bindingResult.rejectValue("cabinet", "error.cabinet",
                    doctorSlotDTO.getDay().getDay() + " кабинет " + doctorSlotDTO.getCabinet().getCabinetNumber() + " занят");
            createModelForAddingSchedule(model);
            return "doctorslots/addSchedule";
        }
        doctorSlotService.addSchedule(doctorSlotDTO.getDoctor().getId(),
                doctorSlotDTO.getDay().getId(),
                doctorSlotDTO.getCabinet().getId());
        return "redirect:/doctorslots";
    }

    public void createModelForAddingSchedule(Model model) {
        model.addAttribute("doctors", doctorService.listAll());
        model.addAttribute("days", dayService.getFirstActualDays(Days.ONE_WEEK));
        model.addAttribute("cabinets", cabinetService.listAll());
    }

    @GetMapping("/delete/{doctorId}/{dayId}")
    public String deleteSoft(@PathVariable Long doctorId,
                             @PathVariable Long dayId) {
        doctorSlotService.deleteSoft(doctorId, dayId);
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
        model.addAttribute("doctorDays", doctorSlotService.groupByDoctorSlot(pageRequest));
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getFirstActualDays(Days.ONE_WEEK));
        return "/doctorslots/makeMeet";
    }

    @PostMapping("/makeMeet/search")
    public String searchGroupSlots(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "12") int pageSize,
                                   @ModelAttribute("doctorSlotSearchForm") DoctorSlotSearchDTO doctorSlotSearchDTO,
                                   Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        model.addAttribute("doctorDays", doctorSlotService.findAmongGroupByDoctorSlot(pageRequest, doctorSlotSearchDTO));
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("days", dayService.getFirstActualDays(Days.ONE_WEEK));
        return "doctorslots/makeMeet";
    }

    @GetMapping("/schedule/actual/{doctorId}")
    public String filterActualDoctorSchedule(@PathVariable Long doctorId,
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
