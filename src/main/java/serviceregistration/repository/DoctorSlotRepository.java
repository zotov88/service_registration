package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.dto.querymodel.*;
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
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet
                    from doctors_slots ds
                        join cabinets c on c.id = ds.cabinet_id
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                    where day >= TIMESTAMP 'today'
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number
                    order by d.day, doc.last_name, s.title
                    """)
    Page<DoctorSlotForSchedule> findActualScheduleGroup(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet
                    from doctors_slots ds
                        join cabinets c on c.id = ds.cabinet_id
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number
                    order by d.day, doc.last_name, s.title
                    """)
    Page<DoctorSlotForSchedule> findArchiveScheduleGroup(PageRequest pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet
                    from doctors_slots ds
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                        join days d on ds.day_id = d.id
                        join cabinets c on c.id = ds.cabinet_id
                    where doc.last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and doc.first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and doc.mid_name ilike'%' || coalesce(:midName, '%')  || '%'
                        and s.title ilike'%' || coalesce(:specialization, '%')  || '%'
                        and to_char(d.day, 'yyyy-mm-dd') ilike'%' || coalesce(:day, '%')  || '%'
                        and cast(c.number as varchar) ilike coalesce(cast(:cabinetNumber as varchar), '%')
                        and d.day >= TIMESTAMP 'today'
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number
                    order by d.day, doc.last_name, s.title
                        """)
    Page<DoctorSlotForSchedule> searchActualScheduleGroup(@Param(value = "lastName") String lastName,
                                                          @Param(value = "firstName") String firstName,
                                                          @Param(value = "midName") String midName,
                                                          @Param(value = "specialization") String specialization,
                                                          @Param(value = "day") String day,
                                                          @Param(value = "cabinetNumber") Integer cabinetNumber,
                                                          Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet
                    from doctors_slots ds
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                        join days d on ds.day_id = d.id
                        join cabinets c on c.id = ds.cabinet_id
                    where doc.last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and doc.first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and doc.mid_name ilike'%' || coalesce(:midName, '%')  || '%'
                        and s.title ilike'%' || coalesce(:specialization, '%')  || '%'
                        and to_char(d.day, 'yyyy-mm-dd') ilike'%' || coalesce(:day, '%')  || '%'
                        and cast(c.number as varchar) ilike coalesce(cast(:cabinetNumber as varchar), '%')
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number
                    order by d.day, doc.last_name, s.title
                        """)
    Page<DoctorSlotForSchedule> searchArchiveScheduleGroup(@Param(value = "lastName") String lastName,
                                                          @Param(value = "firstName") String firstName,
                                                          @Param(value = "midName") String midName,
                                                          @Param(value = "specialization") String specialization,
                                                          @Param(value = "day") String day,
                                                          @Param(value = "cabinetNumber") Integer cabinetNumber,
                                                          Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                    where day >= TIMESTAMP 'today'
                    order by day, doctor_id, slot_id
                    """)
    Page<DoctorSlot> findActualSchedule(Pageable pageable);

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

    @Query(nativeQuery = true,
            value = """
                    select s.time_slot as Slot, ds.is_registered as Registered
                    from doctors_slots ds
                        join slots s on s.id = ds.slot_id
                        join doctors doc on doc.id = ds.doctor_id
                        join days d on ds.day_id = d.id
                    where doc.id = :doctorId
                        and d.id = :dayId
                    order by ds.is_registered desc, s.time_slot
                    """)
    List<SlotRegistered> getSlotsOneDayForDoctor(Long doctorId, Long dayId);


}
