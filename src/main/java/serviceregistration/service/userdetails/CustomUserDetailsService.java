package serviceregistration.service.userdetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import serviceregistration.constants.UserRolesConstants;
import serviceregistration.model.Client;
import serviceregistration.model.Doctor;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final DoctorRepository doctorRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Value("${spring.security.user.roles}")
    private String adminRole;

    public CustomUserDetailsService(ClientRepository clientRepository,
                                    DoctorRepository doctorRepository) {
        this.clientRepository = clientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, adminUserName, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + adminRole)));
        } else {
            Client client = clientRepository.findClientByLogin(username);
            Doctor doctor = doctorRepository.findDoctorByLogin(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (client != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.CLIENT));
                return new CustomUserDetails(client.getId().intValue(), username, client.getPassword(), authorities);
            }
            authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRolesConstants.DOCTOR));
            return new CustomUserDetails(doctor.getId().intValue(), username, doctor.getPassword(), authorities);
        }
    }
}

//        if (username.equals(adminUserName)) {
//            return new CustomUserDetails(null, adminUserName, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + adminRole)));
//        } else {
//            Client client = clientRepository.findClientByLogin(username);
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(client.getRole().getId() == 1L ? "ROLE_" + UserRolesConstants.CLIENT :
//                    "ROLE_" + UserRolesConstants.DOCTOR));
//            return new CustomUserDetails(client.getId().intValue(), username, client.getPassword(), authorities);
