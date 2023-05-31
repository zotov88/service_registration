package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.dto.querymodel.ClientRegistration;
import serviceregistration.dto.querymodel.DoctorRegistration;
import serviceregistration.model.Registration;

import java.util.List;

public interface RegistrationRepository
        extends GenericRepository<Registration> {

    Page<Registration> getAllByClientId(Pageable pageable, Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select id
                    from registrations
                    where client_id = :clientId
                    """)
    List<Long> getAllByClientId(Long clientId);

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
    List<ClientRegistration> getAllRegistrationsByClient(Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select cl.first_name as ClientFirstName, cl.mid_name as ClientMidName, cl.last_name as ClientLastName,
                            d2.day as Day, s.time_slot as Slot, c.number as Cabinet, r.is_active as IsActive
                    from doctors d
                        join doctors_slots ds on d.id = ds.doctor_id
                        join days d2 on ds.day_id = d2.id
                        join cabinets c on c.id = ds.cabinet_id
                        join slots s on ds.slot_id = s.id
                        join registrations r on ds.id = r.doctor_slot_id
                        join clients cl on cl.id = r.client_id
                    where d.id = :doctorId
                    order by d2.day desc
                    """)
    Page<DoctorRegistration> getAllRegistrationsByDoctor(Pageable pageable, Long doctorId);

    @Query(nativeQuery = true,
            value = """
                    select *
                    from registrations
                    where doctor_slot_id = :doctorSlotId
                    """)
    Registration findOnByDoctorSlotId(Long doctorSlotId);

    @Query(nativeQuery = true,
            value = """
                    select r.id
                    from doctors_slots ds
                        join registrations r on ds.id = r.doctor_slot_id
                    where ds.id = :doctorSlotId
                    """)
    Long findIdByDoctorSlotId(Long doctorSlotId);
}
