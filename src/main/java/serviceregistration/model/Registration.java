package serviceregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "registrations_seq", allocationSize = 1)
public class Registration
        extends GenericModel {

    @ManyToOne
    @JoinColumn(name = "doctor_slot_id", nullable = false, foreignKey = @ForeignKey(name = "FK_REGISTRATION_DOCTOR_SLOT"))
    private DoctorSlot doctorSlot;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(name = "FK_REGISTRATION_CLIENT"))
    private Client client;

    @Column(name = "complaint")
    private String complaint;

    @Column(name = "result_meet", nullable = false)
    @Enumerated
    private ResultMeet resultMeet;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;
}
