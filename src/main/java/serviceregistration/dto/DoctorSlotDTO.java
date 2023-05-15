package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.Doctor;
import serviceregistration.model.Slot;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class DoctorSlotDTO extends GenericDTO {

    private Doctor doctor;
    private Day day;
    private Slot slot;
    private Cabinet cabinet;
    private Boolean isRegistered;
    private List<Long> registrationIds;
}
