package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.DoctorSlot;
import serviceregistration.service.DoctorSlotService;
import serviceregistration.service.GenericService;

@RestController
@RequestMapping("/doctorsslots")
@Tag(name = "Расписание врачей", description = "Контроллер для работы с рабочими слотами врачей")
public class DoctorSlotController extends GenericController<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotService doctorSlotService;

    public DoctorSlotController(DoctorSlotService doctorSlotService) {
        super(doctorSlotService);
        this.doctorSlotService = doctorSlotService;
    }
}
