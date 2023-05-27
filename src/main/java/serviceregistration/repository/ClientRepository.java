package serviceregistration.repository;

import serviceregistration.model.Client;

public interface ClientRepository extends GenericRepository<Client> {

    Client findClientByLogin(String login);

    Client findClientByEmail(String email);

    Client findClientByChangePasswordToken(String uuid);

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
