package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.model.Client;

public interface ClientRepository extends GenericRepository<Client> {

    Client findClientByLoginAndIsDeletedFalse(String login);

    Client findClientByEmailAndIsDeletedFalse(String email);

    Client findClientByPolicyAndIsDeletedFalse(Long policy);

    Client findClientByChangePasswordToken(String uuid);

    Client findClientByPhoneAndIsDeletedFalse(Long phone);

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
                    from registrations r
                        join doctors_slots ds on ds.id = r.doctor_slot_id
                        join clients c on c.id = r.client_id
                    where ds.id = :doctorSlotId
                        and ds.is_registered = true
                    """)
//    and r.is_active= true
    Client findClientIdByDoctorSlot(Long doctorSlotId);

    @Query(nativeQuery = true,
            value = """
                    select c.*
                    from clients c
                    where last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                    order by c.last_name, c.first_name, c.mid_name
                    """)
    Page<Client> searchClients(@Param(value = "lastName") String lastName,
                               @Param(value = "firstName") String firstName,
                               @Param(value = "midName") String midName,
                               Pageable page);

    @Query(nativeQuery = true,
            value = """
                    select c.*
                    from clients c
                    where last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                        and is_deleted = false
                    order by c.last_name, c.first_name, c.mid_name
                    """)
    Page<Client> searchClientsWithDeletedFalse(@Param(value = "firstName") String firstName,
                                               @Param(value = "lastName") String lastName,
                                               @Param(value = "midName") String midName,
                                               Pageable page);

    @Query(nativeQuery = true,
            value = """
                    select c.*
                    from clients c
                    where is_deleted = false
                    order by c.last_name, c.first_name, c.mid_name
                    """)
    Page<Client> findListAllClientsWithDeletedFalse(Pageable pageRequest);


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
