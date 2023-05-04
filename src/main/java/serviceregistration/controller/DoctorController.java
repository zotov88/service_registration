package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.Doctor;
import serviceregistration.service.DoctorService;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Врачи", description = "Контроллер для работы с врачами")
public class DoctorController
        extends GenericController<Doctor, DoctorDTO> {

    public DoctorController(DoctorService service) {
        super(service);
    }
}
