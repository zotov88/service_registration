package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.model.User;
import serviceregistration.repository.UserRepository;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByLogin(final String login) {
        return userRepository.findUserByLogin(login);
    }

    public Long findRoleByLogin(final String login) {
        return userRepository.findRoleByLogin(login);
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public void deleteByLogin(final String login) {
        userRepository.deleteUserByLogin(login);
    }

    public void createUser(String login, Long role) {
        User user = new User();
        user.setLogin(login);
        user.setRole(role);
        create(user);
    }
}
