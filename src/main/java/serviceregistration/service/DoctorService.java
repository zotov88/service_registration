package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper,
                         UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public DoctorDTO create(DoctorDTO newObj) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObj.setRole(roleDTO);
        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
        newObj.setCreatedWhen(LocalDateTime.now());
        userService.createUser(newObj.getLogin(), newObj.getRole().getId());
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    @Override
    public void delete(Long id) {
        userService.deleteByLogin(getOne(id).getLogin());
        repository.deleteById(id);
    }

    public DoctorDTO getDoctorByLogin(String login) {
        return mapper.toDTO(((DoctorRepository) repository).findDoctorByLogin(login));
    }

    public Page<DoctorDTO> findDoctors(DoctorSearchDTO doctorSearchDTO,
                                       Pageable pageRequest) {
        Page<Doctor> doctorsPaginated = ((DoctorRepository) repository).searchDoctors(
                doctorSearchDTO.getLastName(),
                doctorSearchDTO.getFirstName(),
                doctorSearchDTO.getMidName(),
                doctorSearchDTO.getSpecialization(),
                pageRequest);
        List<DoctorDTO> result = mapper.toDTOs(doctorsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsPaginated.getTotalElements());
    }

}
