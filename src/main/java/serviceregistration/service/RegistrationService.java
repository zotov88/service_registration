package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;
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

    public Page<RegistrationDTO> getAllByClientId(Pageable pageable, Long clientId) {
        Page<Registration> registrationsPaginated = ((RegistrationRepository)repository).getAllByClientId(pageable, clientId);
        List<RegistrationDTO> result = mapper.toDTOs(registrationsPaginated.getContent());
        return new PageImpl<>(result, pageable, registrationsPaginated.getTotalElements());
    }

    public List<Long> getAllByClientId(Long clientId) {
        return ((RegistrationRepository)repository).getAllByClientId(clientId);
    }

    public Page<UniversalQueryModel> getAllRegistrationsByClient(Long clientId, Pageable pageable) {
        Page<UniversalQueryModel> registrations = ((RegistrationRepository)repository).getAllRegistrationsByClient(clientId, pageable);
        List<UniversalQueryModel> result = registrations.getContent();
        return new PageImpl<>(result, pageable, registrations.getTotalElements());
    }

    public Page<UniversalQueryModel> getAllRegistrationsByDoctor(Pageable pageable, Long doctorId) {
        Page<UniversalQueryModel> doctorRegistrationPage = ((RegistrationRepository)repository).getAllRegistrationsByDoctor(pageable, doctorId);
        List<UniversalQueryModel> result = doctorRegistrationPage.getContent();
        return new PageImpl<>(result, pageable, doctorRegistrationPage.getTotalElements());
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

    public void cancelMeet(Long registrationId) {
        RegistrationDTO registrationDTO = mapper.toDTO(mapper.toEntity(getOne(registrationId)));
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(registrationDTO.getDoctorSlotId());
        doctorSlotDTO.setIsRegistered(false);
        registrationDTO.setIsActive(false);
        registrationDTO.setDeleted(true);
        doctorSlotService.update(doctorSlotDTO);
        update(registrationDTO);
    }

    public Registration getOnByDoctorSlotId(Long id) {
        return ((RegistrationRepository)repository).findOnByDoctorSlotId(id);
    }

    public Long getIdByDoctorSlotId(Long doctorSlotId) {
        return ((RegistrationRepository)repository).findIdByDoctorSlotId(doctorSlotId);
    }

    public List<RegistrationDTO> listAllActiveRegistrationByClientId(Long clientId) {
        return mapper.toDTOs(((RegistrationRepository)repository).findAllActiveRegistrationByClientId(clientId));
    }

    public Long getClientIdByRegistrationId(Long registrationId) {
        return ((RegistrationRepository)repository).findClientIdByRegistrationId(registrationId);
    }

}
