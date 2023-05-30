package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Transactional
@Service
public class DoctorSlotRegistrationService {

    private final DoctorSlotRepository doctorSlotRepository;
    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    public DoctorSlotRegistrationService(DoctorSlotRepository doctorSlotRepository,
                                         RegistrationService registrationService,
                                         RegistrationMapper registrationMapper) {
        this.doctorSlotRepository = doctorSlotRepository;
        this.registrationService = registrationService;
        this.registrationMapper = registrationMapper;
    }

    public void deleteSoft(final Long doctorId, final Long dayId) {
        List<Long> doctorSlotIds = doctorSlotRepository.findIdsWhereActiveSlots(doctorId, dayId);
        for (Long id : doctorSlotIds) {
            RegistrationDTO registrationDTO = registrationMapper.toDTO(registrationService.getOnByDoctorSlotId(id));
            registrationDTO.setIsActive(false);
            registrationService.update(registrationDTO);
        }
        doctorSlotRepository.markAsDeletedSlots(doctorId, dayId);
    }
}
