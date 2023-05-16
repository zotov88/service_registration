package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.Specialization;

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
    private Specialization specialization;
    private RoleDTO role;
    private List<Long> doctorSlotsId;

}
