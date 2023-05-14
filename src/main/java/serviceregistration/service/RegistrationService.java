package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Registration;
import serviceregistration.repository.RegistrationRepository;

@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {
    
    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper) {
        super(repository, mapper);
    }
}
