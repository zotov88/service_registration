package serviceregistration.service.dataconstants;

import serviceregistration.dto.ClientDTO;
import serviceregistration.model.Client;
import serviceregistration.model.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ClientTestData {
    ClientDTO CLIENT_DTO_1 = new ClientDTO(
            "login1",
            "password1",
            1234L,
            "firstName1",
            "midName1",
            "lastName1",
            "MALE",
            30,
            LocalDate.now(),
            89031111111L,
            "email@mail.ru1",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    ClientDTO CLIENT_DTO_2 = new ClientDTO(
            "login2",
            "password2",
            2234L,
            "firstName2",
            "midName2",
            "lastName2",
            "MALE",
            30,
            LocalDate.now(),
            89032222222L,
            "email@mail.ru2",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    ClientDTO CLIENT_DTO_3_DELETED = new ClientDTO(
            "login3",
            "password3",
            3234L,
            "firstName3",
            "midName3",
            "lastName3",
            "MALE",
            30,
            LocalDate.now(),
            89033333333L,
            "email@mail.ru3",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    List<ClientDTO> CLIENT_DTO_LIST = Arrays.asList(CLIENT_DTO_1, CLIENT_DTO_2, CLIENT_DTO_3_DELETED);

    Client CLIENT_1 = new Client(
            "login1",
            "password1",
            1234L,
            "firstName1",
            "midName1",
            "lastName1",
            "MALE",
            30,
            LocalDate.now(),
            89031111111L,
            "email@mail.ru1",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    Client CLIENT_2 = new Client(
            "login2",
            "password2",
            2234L,
            "firstName2",
            "midName2",
            "lastName2",
            "MALE",
            30,
            LocalDate.now(),
            89032222222L,
            "email@mail.ru2",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    Client CLIENT_3 = new Client(
            "login3",
            "password3",
            3234L,
            "firstName3",
            "midName3",
            "lastName3",
            "MALE",
            30,
            LocalDate.now(),
            89033333333L,
            "email@mail.ru3",
            "address",
            new Role(1L, "CLIENT", "cliennt"),
            null,
            new ArrayList<>()
    );
    
    List<Client> CLIENT_LIST = Arrays.asList(CLIENT_1, CLIENT_2, CLIENT_3);
}
