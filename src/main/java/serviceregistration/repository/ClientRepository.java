package serviceregistration.repository;

import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Client;

public interface ClientRepository extends GenericRepository<Client> {

    Client findClientByLogin(String login);

    Client findClientByEmail(String email);

    Client findClientByChangePasswordToken(String uuid);

    @Query(nativeQuery = true,
            value = """
                    select count(*)
                    from clients c
                       join registrations r on c.id = r.client_id
                       join doctors_slots ds on ds.id = r.doctor_slot_id
                       join doctors d on d.id = ds.doctor_id
                       join specializations s on d.specialization_id = s.id
                    where r.is_active = true
                        and s.title ilike :specialization
                        and c.id = :clientId
                    """)
    Long findActiveRegistrationBySpecialization(String specialization, Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select count(*)
                    from clients c
                       join registrations r on c.id = r.client_id
                       join doctors_slots ds on ds.id = r.doctor_slot_id
                       join doctors doc on doc.id = ds.doctor_id
                       join slots s on s.id = ds.slot_id
                    where r.is_active = true
                        and day_id = :dayId
                        and s.id = :slotId
                        and c.id = :clientId
                    """)
    Long findActiveRegistrationByDay(Long dayId, Long slotId, Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select c.*
                    from registrations
                        join doctors_slots ds on ds.id = registrations.doctor_slot_id
                        join clients c on c.id = registrations.client_id
                    where ds.id = :doctorSlotId
                    """)
    Client findClientIdByDoctorSlot(Long doctorSlotId);

//    @Query(nativeQuery = true,
//            value = """
//                    select c.*
//                    from clients c
//                    where c.first_name ilike '%' || coalesce(:firstName, '%') || '%'
//                    and c.last_name ilike '%' || coalesce(:lastName, '%') || '%'
//                    and c.login ilike '%' || coalesce(:login, '%') || '%'
//                     """)
//    Page<Client> searchUsers(String firstName, String lastName, String login, Pageable pageable);
}
