package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.model.Registration;
import serviceregistration.repository.GenericRepository;
import serviceregistration.repository.RegistrationRepository;

@RestController
@RequestMapping("/registrations")
@Tag(name = "Зарегистрированные записи",
        description = "Контроллер для работы со всеми когда-либо зарегистрированными записями к врачам")
public class RegistrationController
        extends GenericController<Registration> {

    private final RegistrationRepository registrationRepository;

    public RegistrationController(GenericRepository<Registration> genericRepository, RegistrationRepository registrationRepository) {
        super(genericRepository);
        this.registrationRepository = registrationRepository;
    }
}
