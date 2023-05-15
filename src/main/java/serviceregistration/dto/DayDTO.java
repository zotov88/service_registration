package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class DayDTO extends GenericDTO {

    private LocalDate day;
    private List<Long> doctorSlotsIds;

}
