package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Registration;
import serviceregistration.querymodel.UniversalQueryModel;

import java.util.List;

public interface RegistrationRepository
        extends GenericRepository<Registration> {

    @Query(nativeQuery = true,
            value = """
                    select r.id as RegistrationId,
                            d.first_name as DoctorFirstName, d.mid_name as DoctorMidName, d.last_name as DoctorLastName,
                            sp.title as Specialization, d2.day as Day, s.time_slot as Slot, c2.number as Cabinet,
                            r.is_active as IsActive
                    from doctors_slots ds
                        join registrations r on ds.id = r.doctor_slot_id
                        join doctors d on d.id = ds.doctor_id
                        join specializations sp on sp.id = d.specialization_id
                        join days d2 on ds.day_id = d2.id
                        join cabinets c2 on c2.id = ds.cabinet_id
                        join slots s on s.id = ds.slot_id
                    where r.client_id = :clientId
                    order by r.is_active desc, d2.day, s.time_slot, sp.title, d.last_name
                    """)
    Page<UniversalQueryModel> getAllRegistrationsByClient(Long clientId, Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select *
                    from registrations
                    where doctor_slot_id = :doctorSlotId
                        and is_active = true
                    """)
    Registration findOnByDoctorSlotId(Long doctorSlotId);

    @Query(nativeQuery = true,
            value = """
                    select r.*
                    from registrations r
                        join clients c on c.id = r.client_id
                    where r.is_deleted = false
                        and r.is_active = true
                        and c.id = :clientId
                    """)
    List<Registration> findAllActiveRegistrationByClientId(Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select r.*
                    from registrations r
                             join doctors_slots ds on r.doctor_slot_id = ds.id
                             join days d on ds.day_id = d.id
                             join slots s on s.id = ds.slot_id
                    where ((now() at time zone 'utc-3') - (d.day + s.time_slot)) > '00:29:00'
                        and r.is_active = true
                    """)
    List<Registration> getListCompletedMeeting();

    @Query(nativeQuery = true,
            value = """
                    select r.*
                    from registrations r
                        join doctors_slots ds on r.doctor_slot_id = ds.id
                        join doctors doc on doc.id = ds.doctor_id
                    where doc.id = :doctorId
                        and r.is_active = true
                    """)
    List<Registration> findRegistrationsByDoctorId(Long doctorId);

    @Query(nativeQuery = true,
            value = """
                    select r.*
                    from doctors_slots ds
                        join registrations r on ds.id = r.doctor_slot_id
                    where ds.id = :doctorSlotId
                        and r.is_active = true
                    """)
    Registration findRegistrationDTOByDoctorSlotId(Long doctorSlotId);
}
