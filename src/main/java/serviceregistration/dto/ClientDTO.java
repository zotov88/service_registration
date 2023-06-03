package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.Client;
import serviceregistration.model.Registration;
import serviceregistration.model.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
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
    private Long phone;
    private String email;
    private String address;
    private Role role;
//    private RoleDTO role;
    private String changePasswordToken;
    private List<Long> registrationIds;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.createdWhen = client.getCreatedWhen();
        this.createdBy = client.getCreatedBy();
        this.deletedWhen = client.getDeletedWhen();
        this.deletedBy = client.getDeletedBy();
        this.isDeleted = client.isDeleted();
        this.login = client.getLogin();
        this.password = client.getPassword();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.midName = client.getMidName();
        this.gender = client.getGender();
        this.age = client.getAge();
        this.birthDate = client.getBirthDate();
        this.phone = client.getPhone();
        this.email = client.getEmail();
        this.address = client.getAddress();
        this.role = client.getRole();
        this.changePasswordToken = client.getChangePasswordToken();
        List<Registration> registrations = client.getRegistrations();
        List<Long> registrationIds = new ArrayList<>();
        registrations.forEach(r -> registrationIds.add(r.getId()));
        this.registrationIds = registrationIds;
    }

}
