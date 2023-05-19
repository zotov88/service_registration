package serviceregistration.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.DoctorSlot;

public interface DoctorSlotRepository
        extends GenericRepository<DoctorSlot> {

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    insert into doctors_slots
                    select nextval('doctor_slot_seq'), null, now(), null, null, null, false,cabinets.id, days.id, doctors.id, slots.id
                    from days
                        cross join slots
                        cross join doctors
                        cross join cabinets
                    where
                        doctors.id = :doctorId
                        and
                        days.id = :dayId
                        and
                        cabinets.id = :cabinetId""")
    void addSchedule(Long doctorId, Long dayId, Long cabinetId);

    DoctorSlot findFirstByCabinetIdAndDayId(Long cabinetId, Long dayId);

    DoctorSlot findFirstByDoctorIdAndDayId(Long doctorId, Long dayId);
}
