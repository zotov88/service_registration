package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class ClientDTO extends GenericDTO {

    private String login;
    private String password;
    private Long policy;
    private String firstName;
    private String lastName;
    private String midName;
    private String gender;
    private Integer age;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private RoleDTO role;
    private List<Long> registrationsId;

}
