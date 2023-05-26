package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import serviceregistration.dto.custommodel.ClientRegistration;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
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

    public List<ClientRegistration> getAllRegistrationsByClient(Long clientId) {
        return ((RegistrationRepository)repository).getAllRegistrationsByClient(clientId);
    }


    public RegistrationDTO registrationSlot(RegistrationDTO registrationDTO) {
        DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(registrationDTO.getDoctorSlotId());
        System.out.println("++++++++++++++++++++++++++++"+registrationDTO.getDoctorSlotId());
        doctorSlotDTO.setIsRegistered(true);
        doctorSlotService.update(doctorSlotDTO);
        registrationDTO.setCreatedWhen(LocalDateTime.now());
        registrationDTO.setIsActive(true);
        registrationDTO.setResultMeet(ResultMeet.SUCCESSFULLY);
        return mapper.toDTO(repository.save(mapper.toEntity(registrationDTO)));
    }

//    public RegistrationDTO registrationSlot(RegistrationDTO registrationDTO) {
//
//    }

//    public Page<DoctorSlotDTO> getAllDoctorSlot(Pageable pageable) {
//        Page<DoctorSlot> doctorSlotsPaginated = doctorSlotRepository.findAllNotLessThanToday(pageable);
//        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotsPaginated.getContent());
//        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
//    }
}
