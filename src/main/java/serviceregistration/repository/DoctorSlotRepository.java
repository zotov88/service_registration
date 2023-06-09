package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.model.DoctorSlot;
import serviceregistration.querymodel.UniversalQueryModel;

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

    DoctorSlot findFirstByCabinetIdAndDayId(Long cabinetId, Long dayId);

    DoctorSlot findFirstByDoctorIdAndDayId(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet, ds.is_deleted as IsDeleted,
                            doc.is_deleted as IsDoctorDeleted
                    from doctors_slots ds
                        join cabinets c on c.id = ds.cabinet_id
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                    where day >= TIMESTAMP 'today'
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number, ds.is_deleted
                    order by ds.is_deleted, d.day, doc.last_name, s.title
                    """)
    Page<UniversalQueryModel> findActualScheduleGroup(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet, ds.is_deleted as IsDeleted
                    from doctors_slots ds
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                        join days d on ds.day_id = d.id
                        join cabinets c on c.id = ds.cabinet_id
                    where doc.last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and doc.first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and doc.mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                        and s.title ilike '%' || coalesce(:specialization, '%')  || '%'
                        and to_char(d.day, 'yyyy-mm-dd') ilike '%' || coalesce(:day, '%')  || '%'
                        and cast(c.number as varchar) ilike coalesce(cast(:cabinetNumber as varchar), '%')
                        and d.day >= TIMESTAMP 'today'
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number, ds.is_deleted
                    order by ds.is_deleted, d.day, doc.last_name, s.title
                        """)
    Page<UniversalQueryModel> searchActualScheduleGroup(@Param(value = "lastName") String lastName,
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
                            s.title as Specialization, d.day as Day, c.number as Cabinet, ds.is_deleted as IsDeleted
                    from doctors_slots ds
                        join cabinets c on c.id = ds.cabinet_id
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number, ds.is_deleted
                    order by ds.is_deleted, d.day, doc.last_name, s.title
                    """)
    Page<UniversalQueryModel> findArchiveScheduleGroup(PageRequest pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.id as DoctorId, d.id as DayId,
                            doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                            s.title as Specialization, d.day as Day, c.number as Cabinet, ds.is_deleted as IsDeleted
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
                    group by doc.id, d.id, doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, c.number, ds.is_deleted
                    order by ds.is_deleted, d.day, doc.last_name, s.title
                        """)
    Page<UniversalQueryModel> searchArchiveScheduleGroup(@Param(value = "lastName") String lastName,
                                                           @Param(value = "firstName") String firstName,
                                                           @Param(value = "midName") String midName,
                                                           @Param(value = "specialization") String specialization,
                                                           @Param(value = "day") String day,
                                                           @Param(value = "cabinetNumber") Integer cabinetNumber,
                                                           Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                        s.title as Specialization, d.day as Day, doc.id as DoctorId, d.id as DayId, c.number as Cabinet
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                        join cabinets c on c.id = ds.cabinet_id
                        join slots s2 on s2.id = ds.slot_id
                    where day >= TIMESTAMP 'today'
                        and ds.is_registered = false
                        and ds.is_deleted = false
                        and d.day + s2.time_slot > (now() at time zone 'utc-3')
                    group by doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, doc.id, d.id, c.number
                    order by d.day, doc.last_name, s.title, c.number
                    """)
    Page<UniversalQueryModel> findGroupByDoctorSlot(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select doc.first_name as DoctorFirstName, doc.mid_name as DoctorMidName, doc.last_name as DoctorLastName,
                        s.title as Specialization, d.day as Day, doc.id as DoctorId, d.id as DayId, c.number as Cabinet
                    from doctors_slots ds
                        join days d on ds.day_id = d.id
                        join doctors doc on ds.doctor_id = doc.id
                        join specializations s on s.id = doc.specialization_id
                        join cabinets c on c.id = ds.cabinet_id
                        join slots s2 on s2.id = ds.slot_id
                    where day >= TIMESTAMP 'today'
                        and doc.last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and doc.first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and doc.mid_name ilike'%' || coalesce(:midName, '%')  || '%'
                        and s.title ilike'%' || coalesce(:specialization, '%')  || '%'
                        and to_char(d.day, 'yyyy-mm-dd') ilike'%' || coalesce(:day, '%')  || '%'
                        and ds.is_registered = false
                        and ds.is_deleted = false
                        and d.day + s2.time_slot > (now() at time zone 'utc-3')
                    group by doc.first_name, doc.mid_name, doc.last_name, s.title, d.day, doc.id, d.id, c.number
                    order by d.day, s.title, doc.last_name, c.number
                    """)
    Page<UniversalQueryModel> searchGroupByDoctorSlot(@Param(value = "lastName") String lastName,
                                            @Param(value = "firstName") String firstName,
                                            @Param(value = "midName") String midName,
                                            @Param(value = "specialization") String specialization,
                                            @Param(value = "day") String day,
                                            Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select d2.day as Day, c.number as Cabinet, d2.id as DayId
                    from doctors d
                        join doctors_slots ds on d.id = ds.doctor_id
                        join days d2 on ds.day_id = d2.id
                        join cabinets c on ds.cabinet_id = c.id
                    where d.id = :doctorId
                    and d2.day >= TIMESTAMP 'today'
                    group by d2.day, c.number, d2.id
                    order by d2.day
                    """)
    Page<UniversalQueryModel> findActualScheduleByDoctorId(Pageable pageable, Long doctorId);

    @Query(nativeQuery = true,
            value = """
                    select d2.day as Day, c.number as Cabinet, d2.id as DayId
                    from doctors d
                        join doctors_slots ds on d.id = ds.doctor_id
                        join days d2 on ds.day_id = d2.id
                        join cabinets c on ds.cabinet_id = c.id
                    where d.id = :doctorId
                    group by d2.day, c.number, d2.id
                    order by d2.day
                    """)
    Page<UniversalQueryModel> findArchiveScheduleByDoctor(Pageable pageable, Long doctorId);

    @Query(nativeQuery = true,
            value = """
                    select ds.id as DoctorSlotId, s.time_slot as Slot, ds.is_registered as Registered
                    from doctors_slots ds
                        join slots s on s.id = ds.slot_id
                        join doctors doc on doc.id = ds.doctor_id
                        join days d on ds.day_id = d.id
                    where doc.id = :doctorId
                        and d.id = :dayId
                    order by ds.is_registered desc, s.time_slot""")
    List<UniversalQueryModel> findSlotsOneDayForDoctor(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select ds.id as DoctorSlotId, s.time_slot as Slot, ds.is_registered as Registered
                    from doctors_slots ds
                        join slots s on s.id = ds.slot_id
                        join doctors doc on doc.id = ds.doctor_id
                        join days d on ds.day_id = d.id
                    where doc.id = :doctorId
                        and d.id = :dayId
                        and d.day + s.time_slot > now()
                    order by ds.is_registered desc, s.time_slot""")
    List<UniversalQueryModel> findSlotsOneDayForClient(Long doctorId, Long dayId);

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    update doctors_slots
                    set is_deleted = true
                    where doctor_id = :doctorId and day_id = :dayId
                    """)
    void markAsDeletedSlots(Long doctorId, Long dayId);

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    update doctors_slots
                    set is_deleted = false
                    where doctor_id = :doctorId and day_id = :dayId
                    """)
    void unMarkAsDeletedSlots(Long doctorId, Long dayId);

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from registrations r
                        join doctors_slots ds on ds.id = r.doctor_slot_id
                        join days d on d.id = ds.day_id
                        join doctors doc on doc.id = ds.doctor_id
                    where r.is_active = true
                        and ds.is_registered = true
                        and doc.id = :doctorId
                        and d.id = :dayId
                    """)
    List<DoctorSlot> findDSWhereRegistrationsIsActive(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select *
                    from doctors_slots
                    where doctor_id = :doctorId
                    """)
    List<DoctorSlot> findAllByDoctorId(Long doctorId);

    @Query(nativeQuery = true,
            value = """
                    select c.number
                    from doctors_slots ds
                        join cabinets c on c.id = ds.cabinet_id
                        join doctors doc on doc.id = ds.doctor_id
                        join days d on d.id = ds.day_id
                    where doc.id = :doctorId
                        and d.id = :dayId
                    """)
    Integer findCabinetByDoctorIdAndDayId(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select ds.*
                    from doctors_slots ds
                        join registrations r on ds.id = r.doctor_slot_id
                        join clients c on c.id = r.client_id
                    where c.id = :clientId
                        and ds.is_registered = true
                        and r.is_active = true
                    """)
    List<DoctorSlot> findAllActiveDoctorSlotsByClientId(Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select ds.id
                    from doctors_slots ds
                             join doctors d on ds.doctor_id = d.id
                             join slots s on s.id = ds.slot_id
                             join days dy on ds.day_id = dy.id
                             join registrations r on ds.id = r.doctor_slot_id
                    where (now() at time zone 'utc-3') > dy.day + s.time_slot
                        and d.id = :doctorId
                        and dy.id = :dayId
                    """)
    List<Long> findPosiblyCancelMeet(Long doctorId, Long dayId);

    @Query(nativeQuery = true,
            value = """
                    select d.day as Day, c.number as Cabinet
                    from doctors_slots ds
                        join doctors doc on ds.doctor_id = doc.id
                        join days d on ds.day_id = d.id
                        join cabinets c on ds.cabinet_id = c.id
                    where doctor_id = :doctorId
                        and to_char(d.day, 'yyyy-mm-dd') ilike'%' || coalesce(:day, '%')  || '%'
                    group by d.day, c.number
                    order by d.day
                    """)
    Page<UniversalQueryModel> findSearchArchiveScheduleByDoctor(Pageable pageable, Long doctorId, String day);


}
