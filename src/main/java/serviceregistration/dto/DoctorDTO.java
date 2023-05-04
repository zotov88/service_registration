package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class DoctorDTO extends GenericDTO {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String midName;
    private SpecializationDTO specialization;
    private RoleDTO role;
    private List<Long> doctorSlotsId;

}
