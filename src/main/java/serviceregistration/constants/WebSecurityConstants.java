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
                    "doctors/search/**"
            );

    List<String> DOCTORS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctors/add"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/doctorslots/**"
//                    "/doctorslots/schedule",
//                    "/doctorslots/addSchedule",
//                    "/doctorslots/deleteSchedule",
//                    "/doctorslots/actual"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "doctorslots/makeMeet"
            );

    List<String> DOCTORSLOTS_PERMISSION_LIST_FOR_DOCTOR = List.of
            (
                    "/doctorslots/mySchedule"
            );

    List<String> CLIENTS_WHITE_LIST = List.of
            (
                    "/login",
                    "/clients/registration",
                    "/clients/remember-password"
            );

    List<String> CLIENTS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "/clients",
                    "/clients/list"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_CLIENT = List.of
            (
                    "/registrations/makeMeet",
                    "/registrations/myList"
            );

    List<String> REGISTRATIONS_PERMISSION_LIST_FOR_ADMIN = List.of
            (
                    "registrations/listAll"
            );

}
