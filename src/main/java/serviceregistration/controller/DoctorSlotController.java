package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.repository.GenericRepository;

@RestController
@RequestMapping("/doctorsslots")
@Tag(name = "Расписание врачей", description = "Контроллер для работы с рабочими слотами врачей")
public class DoctorSlotController
        extends GenericController<DoctorSlot> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorSlotController(GenericRepository<DoctorSlot> genericRepository, DoctorSlotRepository doctorSlotRepository) {
        super(genericRepository);
        this.doctorSlotRepository = doctorSlotRepository;
    }
}
