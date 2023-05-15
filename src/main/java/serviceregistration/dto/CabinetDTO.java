package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class CabinetDTO {

    private Long id;
    private Integer cabinetNumber;
    private String cabinetDescription;
    private List<Long> doctorSlotsIds;
}
