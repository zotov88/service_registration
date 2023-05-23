package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByLogin(String login);

    @Query(nativeQuery = true,
            value = "select role from users where login = :login")
    Long findRoleByLogin(String login);

    void deleteUserByLogin(String login);
}
