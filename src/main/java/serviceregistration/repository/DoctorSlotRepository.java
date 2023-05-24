package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.customcomponent.DoctorDay;
import serviceregistration.model.DoctorSlot;

import java.util.List;

public interface DoctorSlotRepository extends GenericRepository<DoctorSlot> {

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    insert into doctors_slots
                    select nextval('doctor_slot_seq'), null, now(), null, null, false, false,cabinets.id, days.id, doctors.id, slots.id
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

    void deleteAllByDoctorIdAndDayId(Long doctorId, Long dayId);

    DoctorSlot findFirstByCabinetIdAndDayId(Long cabinetId, Long dayId);

    DoctorSlot findFirstByDoctorIdAndDayId(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                    where day >= TIMESTAMP 'today'
                    order by day_id
                    """)
    Page<DoctorSlot> findAllNotLessThanToday(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select dc.id as DoctorId, d.id as DayId
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                        join doctors dc on ds.doctor_id = dc.id
                    where day > TIMESTAMP 'today' and ds.is_registered = false
                    group by dc.id, d.id""")
    List<DoctorDay> groupByDoctorSlot();

}
