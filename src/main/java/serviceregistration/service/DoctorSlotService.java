package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             DoctorSlotMapper doctorSlotMapper) {
        super(doctorSlotRepository, doctorSlotMapper);
    }
}
