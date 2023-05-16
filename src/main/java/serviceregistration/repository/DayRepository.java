package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Day;

public interface DayRepository extends JpaRepository<Day, Long> {
}
