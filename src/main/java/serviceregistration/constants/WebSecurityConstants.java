package serviceregistration.constants;

import java.util.List;

public interface WebSecurityConstants {

    List<String> RESOURCES_WHITE_LIST = List.of
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

    List<String> DOCTORS_WHITE_LIST = List.of
            (
                    "/doctors",
                    "/doctors/search/**",
                    "/doctors/{id}"
            );

    List<String> DOCTORS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctors/add"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctorslots/schedule",
                    "/doctorslots/search",
                    "/doctorslots/addSchedule",
                    "/doctorslots/deleteSchedule",
                    "/doctorslots/actual"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/doctorslots/makeMeet"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST = List.of
            (
                    "/doctorslots/doctor-schedule/**"
            );

    List<String> CLIENTS_WHITE_LIST = List.of
            (
                    "/login",
                    "/clients/registration",
                    "/clients/remember-password",

                    "/clients/profile/{id}"
            );

    List<String> CLIENTS_PERMISSION_LIST = List.of
            (
                    "/clients/profile/registrations/client-slots/{id}"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/registrations/makeMeet",
                    "/registrations/myList"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/registrations/listAll"
            );

}
