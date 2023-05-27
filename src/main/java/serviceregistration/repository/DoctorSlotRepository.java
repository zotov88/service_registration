package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.dto.querymodel.DoctorDay;
import serviceregistration.dto.querymodel.DoctorSchedule;
import serviceregistration.dto.querymodel.DoctorSlotIdTimeSlot;
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
                    order by day, doctor_id, slot_id
                    """)
    Page<DoctorSlot> findAllSchedule(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                    where day >= TIMESTAMP 'today'
                    order by day, doctor_id, slot_id
                    """)
    Page<DoctorSlot> findActualSchedule(PageRequest pageable);

    @Query(nativeQuery = true,
            value = """
                    select dc.first_name as DoctorFirstName, dc.mid_name as DoctorMidName, dc.last_name as DoctorLastName,
                        s.title as Specialization, d.day as Day, dc.id as DoctorId, d.id as DayId
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                        join doctors dc on ds.doctor_id = dc.id
                        join specializations s on s.id = dc.specialization_id
                    where day > TIMESTAMP 'today'
                        and ds.is_registered = false
                    group by dc.first_name, dc.mid_name, dc.last_name, s.title, d.day, dc.id, d.id""")
    Page<DoctorDay> groupByDoctorSlot(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select dc.first_name as DoctorFirstName, dc.mid_name as DoctorMidName, dc.last_name as DoctorLastName,
                        s.title as Specialization, d.day as Day, dc.id as DoctorId, d.id as DayId
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                        join doctors dc on ds.doctor_id = dc.id
                        join specializations s on s.id = dc.specialization_id
                    where day > TIMESTAMP 'today'
                        and ds.is_registered = false
                    group by dc.first_name, dc.mid_name, dc.last_name, s.title, d.day, dc.id, d.id""")
    List<DoctorDay> groupByDoctorSlot();

    @Query(nativeQuery = true,
            value = """
                    select id
                    from doctors_slots
                    where doctor_id = :doctorId
                        and day_id = :dayId
                        and is_registered = false""")
    List<Long> findAllTimeForDoctorDay(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select ds.id as RegistrationId, s.time_slot as Slot
                    from doctors_slots ds
                        join slots s on s.id = ds.slot_id
                    where ds.doctor_id = :doctorId
                        and ds.day_id = :dayId
                        and ds.is_registered = false;
                    """)
    List<DoctorSlotIdTimeSlot> findAllDoctorslotIdsAndTimeSlotsFree(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select d2.day as Day, s.time_slot as Slot, c.number as Cabinet, ds.is_registered as IsActive
                    from doctors d
                        join doctors_slots ds on d.id = ds.doctor_id
                        join days d2 on ds.day_id = d2.id
                        join slots s on ds.slot_id = s.id
                        join cabinets c on ds.cabinet_id = c.id
                        left join registrations r on ds.id = r.doctor_slot_id
                    where d.id = :doctorId
                    and d2.day > TIMESTAMP 'today'
                    order by d2.day, s.time_slot
                    """)
    Page<DoctorSchedule> findScheduleByDoctorId(Pageable pageable, Long doctorId);
}
