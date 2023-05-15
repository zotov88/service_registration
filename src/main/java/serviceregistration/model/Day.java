package serviceregistration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @OneToMany(mappedBy = "day")
    private List<DoctorSlot> doctorSlots;
}
