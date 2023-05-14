package serviceregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class SpecializationDTO {

    private Long id;
    private String titleSpecialization;
    private String specializationDescription;
}
