package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Doctor;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDayCabinetDTO {

    private Doctor doctor;
    private LocalDate day;
    private Cabinet cabinet;

}
