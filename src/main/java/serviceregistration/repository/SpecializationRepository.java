package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

}
