package serviceregistration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import serviceregistration.service.userdetails.CustomUserDetailsService;

import java.util.List;

import static serviceregistration.constants.UserRolesConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    private final List<String> RESOURCES_WHITE_LIST = List.of
            (
                    "/resources/**",
                    "/static/**",
                    "/js/**",
                    "/images/**",
                    "/css/**",
                    "/",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
            );

    private final List<String> DOCTORS_WHITE_LIST = List.of
            (
                    "/doctors"
            );

    private final List<String> DOCTORS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctors/add"
            );

    private final List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (

                    "/doctorslots",
                    "/doctorslots/schedule",
                    "/doctorslots/addSchedule",
                    "/doctorslots/deleteSchedule"
            );

//    private final List<String> DOCTORSLOTS_PERMISSION_LIST = List.of
//            (
//                    "doctorslots/makeMeet"
//            );

    private final List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_DOCTOR = List.of
            (
                    "/doctorslots/mySchedule"
            );

    private final List<String> CLIENTS_WHITE_LIST = List.of
            (
                    "/login",
                    "/clients/registration",
                    "/clients/remember-password"
            );

    private final List<String> CLIENTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/clients",
                    "/clients/list"
            );

    private final List<String> REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT = List.of
            (

                    "/registrations/makeMeet",
                    "/registrations/myList"
            );

    private final List<String> REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "registrations/listAll"
            );

    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder,
                             CustomUserDetailsService customUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .disable()
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(DOCTORS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(CLIENTS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(DOCTORS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
//                        .requestMatchers(DOCTORSLOTS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, CLIENT)
                        .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                        .requestMatchers(DOCTORSLOTS_PERMISSION_LIST_FOR_DOCTOR.toArray(String[]::new)).hasRole(DOCTOR)

                        .requestMatchers(CLIENTS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                        .requestMatchers(REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT.toArray(String[]::new)).hasRole(CLIENT)
                        .requestMatchers(REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN.toArray(String[]::new)).hasRole(ADMIN)
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                );
        return http.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
