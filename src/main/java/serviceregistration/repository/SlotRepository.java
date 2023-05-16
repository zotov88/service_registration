package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serviceregistration.model.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {
}
