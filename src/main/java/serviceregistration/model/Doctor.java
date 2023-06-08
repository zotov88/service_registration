package serviceregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "doctors",
        uniqueConstraints = @UniqueConstraint(name = "uniqueLogin", columnNames = "login"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "doctors_seq", allocationSize = 1)
public class Doctor
        extends GenericModel
        implements Userable {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "mid_name")
    private String midName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTORS_SPECIALIZATIONS"))
    private Specialization specialization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTORS_ROLES"))
    private Role role;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor", cascade = CascadeType.REMOVE)
    private List<DoctorSlot> doctorSlots;

}
