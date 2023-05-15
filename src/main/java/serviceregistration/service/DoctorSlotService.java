package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

import java.time.LocalDate;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
    }

    public void getSchedule(Long dayId, String name, Integer cabinet) {
         doctorSlotRepository.addSchedule(dayId, name, cabinet);
    }

//    @Override
//    public DoctorSlotDTO create(DoctorSlotDTO newObj) {
//
//    }
}
