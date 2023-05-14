package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.Doctor;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class DoctorMapper extends GenericMapper<Doctor, DoctorDTO> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorMapper(ModelMapper modelMapper,
                        DoctorSlotRepository doctorSlotRepository) {
        super(Doctor.class, DoctorDTO.class, modelMapper);
        this.doctorSlotRepository = doctorSlotRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Doctor.class, DoctorDTO.class)
                .addMappings(m -> m.skip(DoctorDTO::setDoctorSlotsId)).setPostConverter(toEntityConverter());
        modelMapper.createTypeMap(DoctorDTO.class, Doctor.class)
                .addMappings(m -> m.skip(Doctor::setDoctorSlots)).setPostConverter(toDTOConverter());
    }

    @Override
    protected void mapSpecificFields(DoctorDTO src, Doctor dst) {
        if (!Objects.isNull(src.getDoctorSlotsId())) {
            dst.setDoctorSlots(doctorSlotRepository.findAllById(src.getDoctorSlotsId()));
        } else {
            dst.setDoctorSlots(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Doctor src, DoctorDTO dst) {
        dst.setDoctorSlotsId(getIds(src));
    }

    @Override
    protected List<Long> getIds(Doctor entity) {
        if (Objects.isNull(entity)) {
            return Collections.emptyList();
        } else {
            List<Long> list = new ArrayList<>();
            for (DoctorSlot doctorSlot : entity.getDoctorSlots()) {
                list.add(doctorSlot.getId());
            }
            return list;
        }
    }
}
