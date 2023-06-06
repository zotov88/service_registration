package serviceregistration.dto;

import lombok.*;
import serviceregistration.model.Doctor;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Role;
import serviceregistration.model.Specialization;

import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorDTO extends GenericDTO {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String midName;
    private Specialization specialization;
    private Role role;
    private String changePasswordToken;
    private List<Long> doctorSlotsIds;

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.createdWhen = doctor.getCreatedWhen();
        this.createdBy = doctor.getCreatedBy();
        this.deletedWhen = doctor.getDeletedWhen();
        this.deletedBy = doctor.getDeletedBy();
        this.isDeleted = doctor.isDeleted();
        this.login = doctor.getLogin();
        this.password = doctor.getPassword();
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.midName = doctor.getMidName();
        this.specialization = doctor.getSpecialization();
        this.role = doctor.getRole();
        List<DoctorSlot> doctorSlots = doctor.getDoctorSlots();
        List<Long> doctorSlotsIds = new ArrayList<>();
        doctorSlots.forEach(ds -> doctorSlotsIds.add(ds.getId()));
        this.doctorSlotsIds = doctorSlotsIds;
    }
}
