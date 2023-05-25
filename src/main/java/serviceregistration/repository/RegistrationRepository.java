package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import serviceregistration.model.Registration;

public interface RegistrationRepository
        extends GenericRepository<Registration> {

    Page<Registration> getAllByClientId(Pageable pageable, Long clientId);
}
