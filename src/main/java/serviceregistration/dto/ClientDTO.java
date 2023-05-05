package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class ClientDTO extends GenericDTO {

    private String login;
    private String password;
    private Integer policy;
    private String firstName;
    private String lastName;
    private String midName;
    private String gender;
    private Integer age;
    private LocalDateTime birthDate;
    private String phone;
    private String email;
    private String address;
    private RoleDTO role;
    private List<Long> registrationsId;

}
