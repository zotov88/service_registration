package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.model.Doctor;
import serviceregistration.repository.DoctorRepository;
import serviceregistration.repository.GenericRepository;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Врачи", description = "Контроллер для работы с врачами")
public class DoctorController
        extends GenericController<Doctor> {

    private final DoctorRepository doctorRepository;

    public DoctorController(GenericRepository<Doctor> genericRepository, DoctorRepository doctorRepository) {
        super(genericRepository);
        this.doctorRepository = doctorRepository;
    }

}
