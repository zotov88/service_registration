package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.repository.RegistrationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {

    private final DoctorSlotService doctorSlotService;
    
    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper,
                               DoctorSlotService doctorSlotService) {
        super(repository, mapper);
        this.doctorSlotService = doctorSlotService;
    }

    public Page<UniversalQueryModel> getAllRegistrationsByClient(Long clientId, Pageable pageable) {
        Page<UniversalQueryModel> registrations = ((RegistrationRepository)repository).getAllRegistrationsByClient(clientId, pageable);
        List<UniversalQueryModel> result = registrations.getContent();
        return new PageImpl<>(result, pageable, registrations.getTotalElements());
    }

    public void registrationSlot(RegistrationDTO registrationDTO) {
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(registrationDTO.getDoctorSlotId());
        doctorSlotDTO.setIsRegistered(true);
        doctorSlotService.update(doctorSlotDTO);
        registrationDTO.setCreatedWhen(LocalDateTime.now());
        registrationDTO.setIsActive(true);
        registrationDTO.setResultMeet(ResultMeet.SUCCESSFULLY);
        update(registrationDTO);
    }

    public RegistrationDTO cancelMeet(Long registrationId) {
        RegistrationDTO registrationDTO = getOne(registrationId);
        doctorSlotService.setRegisteredDoctorSlotFalse(registrationDTO.getDoctorSlotId());
        registrationDTO.setIsActive(false);
        registrationDTO.setDeleted(true);
        return update(registrationDTO);
    }

    public RegistrationDTO getOnByDoctorSlotId(Long id) {
        return mapper.toDTO(((RegistrationRepository)repository).findOnByDoctorSlotId(id));
    }

    public List<RegistrationDTO> listAllActiveRegistrationByClientId(Long clientId) {
        return mapper.toDTOs(((RegistrationRepository)repository).findAllActiveRegistrationByClientId(clientId));
    }

    public void setCompletedMeetingToFalse() {
        List<RegistrationDTO> registrationDTOS = mapper.toDTOs(((RegistrationRepository)repository).getListCompletedMeeting());
        for (RegistrationDTO registrationDTO : registrationDTOS) {
            registrationDTO.setIsActive(false);
            update(registrationDTO);
        }
    }

    public List<RegistrationDTO> getRegistrationsByDoctorIdWhereIsActive(Long doctorId) {
        return mapper.toDTOs(((RegistrationRepository)repository).findRegistrationsByDoctorId(doctorId));
    }

    public RegistrationDTO getRegistrationDTOByDoctorSlotId(Long doctorSlotId) {
        return mapper.toDTO(((RegistrationRepository) repository).findRegistrationDTOByDoctorSlotId(doctorSlotId));
    }

    public List<RegistrationDTO> getMeetingsBeforeDay() {
        return mapper.toDTOs(((RegistrationRepository)repository).findMeetingsBeforeDay());
    }
}
