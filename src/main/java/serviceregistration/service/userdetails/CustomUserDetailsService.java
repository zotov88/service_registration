package serviceregistration.service.userdetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serviceregistration.model.Userable;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.DoctorRepository;
import serviceregistration.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final DoctorRepository doctorRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Value("${spring.security.user.roles}")
    private String adminRole;

    public CustomUserDetailsService(UserRepository userRepository,
                                    ClientRepository clientRepository,
                                    DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, adminUserName, adminPassword,
                    List.of(new SimpleGrantedAuthority("ROLE_" + adminRole)));
        } else {
            return getUserDetails(userRepository.findRoleByLogin(username) == 1
                            ? clientRepository.findClientByLoginAndIsDeletedFalse(username)
                            : doctorRepository.findDoctorByLoginAndIsDeletedFalse(username),
                            username);
        }
    }

    public UserDetails getUserDetails(final Userable user, final String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getTitle()));
        return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
    }
}
