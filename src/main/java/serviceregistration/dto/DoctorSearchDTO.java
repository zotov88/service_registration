package serviceregistration.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchDTO {

    private String firstName;
    private String lastName;
    private String midName;
    private String specialization;
}
