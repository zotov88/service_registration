package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Registration;
import serviceregistration.repository.RegistrationRepository;

import java.util.List;

@Service
public class RegistrationService extends GenericService<Registration, RegistrationDTO> {
    
    public RegistrationService(RegistrationRepository repository,
                               RegistrationMapper mapper) {
        super(repository, mapper);
    }

    public Page<RegistrationDTO> getAllByClientId(Pageable pageable, Long clientId) {
        Page<Registration> registrationsPaginated = ((RegistrationRepository)repository).getAllByClientId(pageable, clientId);
        List<RegistrationDTO> result = mapper.toDTOs(registrationsPaginated.getContent());
        return new PageImpl<>(result, pageable, registrationsPaginated.getTotalElements());
    }

//    public Page<DoctorSlotDTO> getAllDoctorSlot(Pageable pageable) {
//        Page<DoctorSlot> doctorSlotsPaginated = doctorSlotRepository.findAllNotLessThanToday(pageable);
//        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotsPaginated.getContent());
//        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
//    }
}
