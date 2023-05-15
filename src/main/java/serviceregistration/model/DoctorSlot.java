package serviceregistration.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors_slots",
    uniqueConstraints = @UniqueConstraint(name = "time_slot", columnNames = {"day_id", "slot_id", "doctor_id", "cabinet_id"}))
@SequenceGenerator(name = "default_generator", sequenceName = "doctor_slot_seq", allocationSize = 1)
public class DoctorSlot extends GenericModel {

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTOR_SLOT_DOCTOR"))
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTOR_SLOT_DAY"))
    private Day day;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTOR_SLOT_SLOT"))
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "cabinet_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTOR_SLOT_CABINET"))
    private Cabinet cabinet;

    @OneToMany(mappedBy = "doctorSlot")
    private List<Registration> registrations;

    @Column(name = "is_registered", nullable = false, columnDefinition = "boolean default false")
    private Boolean isRegistered;

}
