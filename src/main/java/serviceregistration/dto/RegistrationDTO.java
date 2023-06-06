package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationDTO extends GenericDTO {

    private String complaint;
    private ResultMeet resultMeet;
    private Boolean isActive;
    private Long doctorSlotId;
    private Long clientId;

    public RegistrationDTO(Registration registration) {
        this.id = registration.getId();
        this.createdWhen = registration.getCreatedWhen();
        this.createdBy = registration.getCreatedBy();
        this.deletedWhen = registration.getDeletedWhen();
        this.deletedBy = registration.getDeletedBy();
        this.isDeleted = registration.isDeleted();
        this.complaint = registration.getComplaint();
        this.resultMeet = registration.getResultMeet();
        this.isActive = registration.getIsActive();
        this.doctorSlotId = registration.getDoctorSlot().getId();
        this.clientId = registration.getClient().getId();
    }

}
