package serviceregistration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.DoctorSlot;
import serviceregistration.service.DoctorSlotService;

@RestController
@RequestMapping("/doctorsslots")
@Tag(name = "Расписание врачей", description = "Контроллер для работы с рабочими слотами врачей")
public class DoctorSlotController extends GenericController<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotService doctorSlotService;

    public DoctorSlotController(DoctorSlotService doctorSlotService) {
        super(doctorSlotService);
        this.doctorSlotService = doctorSlotService;
    }

    @Operation(description = "Добавить расписание для врача")
    @RequestMapping(value = "/addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void schedule(@RequestParam(value = "dayId") Long dayId,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "cabinet") Integer cabinet) {
        doctorSlotService.getSchedule(dayId, name, cabinet);
    }
}
