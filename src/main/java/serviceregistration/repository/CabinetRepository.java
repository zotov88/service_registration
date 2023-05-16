package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Cabinet;

public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
}
