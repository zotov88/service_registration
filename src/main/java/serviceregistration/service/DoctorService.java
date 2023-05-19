package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper) {
        super(repository, mapper);
    }

    public DoctorDTO create(DoctorDTO newObj) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObj.setRole(roleDTO);
        newObj.setCreatedWhen(LocalDateTime.now());
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    public DoctorDTO getDoctorByLogin(String login) {
        return mapper.toDTO(((DoctorRepository)repository).findDoctorByLogin(login));
    }
}
