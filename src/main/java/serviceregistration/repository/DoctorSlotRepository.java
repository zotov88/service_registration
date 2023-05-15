package serviceregistration.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.DoctorSlot;

public interface DoctorSlotRepository
        extends GenericRepository<DoctorSlot> {

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into doctors_slots(doctor_id, day_id, slot_id, cabinet_id)\n" +
                    "select doctors.id, days.id, slots.id, cabinets.id " +
                    "from days " +
                    "cross join slots " +
                    "cross join doctors " +
                    "cross join cabinets " +
                    "where days.id = :dayId " +
                    "and doctors.id = :doctorId " +
                    "and cabinets.id = :cabinetId")
    void addSchedule(Long dayId, Long doctorId, Long cabinetId);
}
