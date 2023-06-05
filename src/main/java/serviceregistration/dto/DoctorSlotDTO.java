package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
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

    public DoctorSlotDTO(DoctorSlot doctorSlot) {
        this.id = doctorSlot.getId();
        this.createdWhen = doctorSlot.getCreatedWhen();
        this.createdBy = doctorSlot.getCreatedBy();
        this.deletedWhen = doctorSlot.getDeletedWhen();
        this.deletedBy = doctorSlot.getDeletedBy();
        this.isDeleted = doctorSlot.isDeleted();
        this.doctor = doctorSlot.getDoctor();
        this.day = doctorSlot.getDay();
        this.cabinet = doctorSlot.getCabinet();
        this.isRegistered = doctorSlot.getIsRegistered();
        List<Registration> registrations = doctorSlot.getRegistrations();
        List<Long> registrationIds = new ArrayList<>();
        registrations.forEach(r -> registrationIds.add(r.getId()));
        this.registrationIds = registrationIds;
    }
}
