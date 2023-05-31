package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.dto.DoctorSlotDTO;
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
    private final DoctorSlotService doctorSlotService;
    private final RegistrationService registrationService;

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper,
                         UserService userService,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         DoctorSlotService doctorSlotService,
                         RegistrationService registrationService) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.doctorSlotService = doctorSlotService;
        this.registrationService = registrationService;
    }

    public DoctorDTO create(DoctorDTO doctorDTO) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        doctorDTO.setRole(roleDTO);
        doctorDTO.setPassword(bCryptPasswordEncoder.encode(doctorDTO.getPassword()));
        doctorDTO.setCreatedWhen(LocalDateTime.now());
        if (userService.findUserByLogin(doctorDTO.getLogin()) == null) {
            userService.createUser(doctorDTO.getLogin(), doctorDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(doctorDTO)));
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

    public void softDelete(Long doctorId) {
        DoctorDTO doctorDTO = getOne(doctorId);
        List<DoctorSlotDTO> allSchedule = doctorSlotService.getAllByDoctorId(doctorDTO.getId());
        List<Long> registrationIds = doctorSlotService.getAllRegistrationsByDoctorId(doctorDTO.getId());
        for (Long id : registrationIds) {
            registrationService.cancelMeet(id);
        }
        for (DoctorSlotDTO schedule : allSchedule) {
            schedule.setDeleted(true);
            schedule.setIsRegistered(false);
            doctorSlotService.update(schedule);
        }
        doctorDTO.setDeleted(true);
        repository.save(mapper.toEntity(doctorDTO));
    }

    public void restore(Long doctorId) {
        DoctorDTO doctorDTO = getOne(doctorId);
        List<DoctorSlotDTO> allSchedule = doctorSlotService.getAllByDoctorId(doctorDTO.getId());
        for (DoctorSlotDTO schedule : allSchedule) {
            schedule.setDeleted(false);
            doctorSlotService.update(schedule);
        }
        doctorDTO.setDeleted(false);
        repository.save(mapper.toEntity(doctorDTO));
    }

    public Page<DoctorDTO> listAllSort(Pageable pageable) {
        Page<Doctor> doctorsSortPaginated = ((DoctorRepository) repository).findDoctorsSort(pageable);
        List<DoctorDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageable, doctorsSortPaginated.getTotalElements());
    }
}
