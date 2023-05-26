package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import serviceregistration.model.ResultMeet;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class RegistrationDTO extends GenericDTO {

    private String complaint;
    private ResultMeet resultMeet;
    private Boolean isActive;
    private Long doctorSlotId;
    private Long clientId;

}
