package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Transactional
@Service
public class DoctorSlotRegistrationService {

    private final DoctorSlotService doctorSlotService;
    private final DoctorSlotRepository doctorSlotRepository;
    private final DoctorSlotMapper doctorSlotMapper;
    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    public DoctorSlotRegistrationService(DoctorSlotService doctorSlotService,
                                         DoctorSlotRepository doctorSlotRepository,
                                         DoctorSlotMapper doctorSlotMapper,
                                         RegistrationService registrationService,
                                         RegistrationMapper registrationMapper) {
        this.doctorSlotService = doctorSlotService;
        this.doctorSlotRepository = doctorSlotRepository;
        this.doctorSlotMapper = doctorSlotMapper;
        this.registrationService = registrationService;
        this.registrationMapper = registrationMapper;
    }

    public void deleteSoft(final Long doctorId, final Long dayId) {
        List<DoctorSlotDTO> doctorSlotDTOS = doctorSlotMapper.toDTOs(doctorSlotRepository.findDSWhereRegistrationsIsActive(doctorId, dayId));
        for (DoctorSlotDTO doctorSlotDTO : doctorSlotDTOS) {
            RegistrationDTO registrationDTO = registrationMapper.toDTO(registrationService.getOnByDoctorSlotId(doctorSlotDTO.getId()));
            registrationDTO.setIsActive(false);
            doctorSlotDTO.setIsRegistered(false);
            doctorSlotService.update(doctorSlotDTO);
            registrationService.update(registrationDTO);
        }
        doctorSlotRepository.markAsDeletedSlots(doctorId, dayId);
    }
}
