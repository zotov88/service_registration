package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.RegistrationRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class DoctorSlotMapper extends GenericMapper<DoctorSlot, DoctorSlotDTO> {

    private final RegistrationRepository registrationRepository;

    public DoctorSlotMapper(ModelMapper modelMapper,
                            RegistrationRepository registrationRepository) {
        super(DoctorSlot.class, DoctorSlotDTO.class, modelMapper);
        this.registrationRepository = registrationRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(DoctorSlot.class, DoctorSlotDTO.class)
                .addMappings(m -> m.skip(DoctorSlotDTO::setRegistrationIds)).setPostConverter(toEntityConverter());
        modelMapper.createTypeMap(DoctorSlotDTO.class, DoctorSlot.class)
                .addMappings(m -> m.skip(DoctorSlot::setRegistrations)).setPostConverter(toDTOConverter());
    }

    @Override
    protected void mapSpecificFields(DoctorSlotDTO src, DoctorSlot dst) {
        if (!Objects.isNull(src.getRegistrationIds())) {
            dst.setRegistrations(registrationRepository.findAllById(src.getRegistrationIds()));
        } else {
            dst.setRegistrations(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(DoctorSlot src, DoctorSlotDTO dst) {
        dst.setRegistrationIds(getIds(src));
    }

    @Override
    protected List<Long> getIds(DoctorSlot entity) {
        return null;
    }
}
