package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.Client;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.ResultMeet;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDTO extends GenericDTO {

    private DoctorSlot doctorSlot;
    private Client client;
    private String complaint;
    private ResultMeet resultMeet;
    private Boolean isActive;
}
