package serviceregistration.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSlotSearchDTO {

    private String firstName;
    private String lastName;
    private String midName;
    private String specialization;
    private String day;
    private Integer cabinetNumber;
    private Boolean isActive;

}
