package serviceregistration.REST.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.service.RegistrationService;

@RestController
@RequestMapping("/registrations")
@Tag(name = "Зарегистрированные записи",
        description = "Контроллер для работы со всеми когда-либо зарегистрированными записями к врачам")
public class RegistrationController extends GenericController<Registration, RegistrationDTO> {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        super(registrationService);
        this.registrationService = registrationService;
    }
}
