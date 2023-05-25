package serviceregistration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DoctorSearchDTO {

    private String firstName;
    private String lastName;
    private String midName;
    private String specialization;

}
