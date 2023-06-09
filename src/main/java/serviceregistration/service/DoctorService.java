package serviceregistration.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.model.Role;
import serviceregistration.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.util.List;

import static serviceregistration.constants.MailConstants.MAIL_BODY_FOR_REGISTRATION_CANCEL;
import static serviceregistration.constants.MailConstants.MAIL_SUBJECT_FOR_REGISTRATION_CANCEL;

@Service
public class DoctorService extends GenericService<Doctor, DoctorDTO> {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DoctorSlotService doctorSlotService;
    private final RegistrationService registrationService;
    private final MailSenderService mailSenderService;

    public DoctorService(DoctorRepository repository,
                         DoctorMapper mapper,
                         UserService userService,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         DoctorSlotService doctorSlotService,
                         RegistrationService registrationService,
                         @Lazy MailSenderService mailSenderService) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.doctorSlotService = doctorSlotService;
        this.registrationService = registrationService;
        this.mailSenderService = mailSenderService;
    }

    public DoctorDTO create(DoctorDTO doctorDTO) {
        Role role = new Role();
        role.setId(2L);
        doctorDTO.setRole(role);
        doctorDTO.setPassword(bCryptPasswordEncoder.encode(doctorDTO.getPassword()));
        doctorDTO.setCreatedWhen(LocalDateTime.now());
        if (userService.findUserByLogin(doctorDTO.getLogin()) == null) {
            userService.createUser(doctorDTO.getLogin(), doctorDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(doctorDTO)));
    }

    public DoctorDTO update(DoctorDTO updObj) {
        return mapper.toDTO(repository.save(mapper.toEntity(updObj)));
    }

    @Override
    public void delete(Long id) {
        userService.deleteByLogin(getOne(id).getLogin());
        repository.deleteById(id);
    }

    public DoctorDTO getDoctorByLogin(String login) {
        return mapper.toDTO(((DoctorRepository) repository).findDoctorByLogin(login));
    }

    public Page<DoctorDTO> searchDoctorsSort(DoctorSearchDTO doctorSearchDTO,
                                             Pageable pageRequest) {
        Page<Doctor> doctorsPaginated = ((DoctorRepository) repository).findSearchDoctorsSort(
                doctorSearchDTO.getLastName(),
                doctorSearchDTO.getFirstName(),
                doctorSearchDTO.getMidName(),
                doctorSearchDTO.getSpecialization(),
                pageRequest);
        List<DoctorDTO> result = mapper.toDTOs(doctorsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsPaginated.getTotalElements());
    }

    public Page<DoctorDTO> searchDoctorsSortWithDeletedFalse(DoctorSearchDTO doctorSearchDTO,
                                                             Pageable pageRequest) {
        Page<Doctor> doctorsPaginated = ((DoctorRepository) repository).findSearchDoctorsSortWithDeletedFalse(
                doctorSearchDTO.getLastName(),
                doctorSearchDTO.getFirstName(),
                doctorSearchDTO.getMidName(),
                doctorSearchDTO.getSpecialization(),
                pageRequest);
        List<DoctorDTO> result = mapper.toDTOs(doctorsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsPaginated.getTotalElements());
    }

    public void softDelete(Long doctorId) {
        List<RegistrationDTO> registrationDTOS = registrationService.getRegistrationsByDoctorIdWhereIsActive(doctorId);
        for (RegistrationDTO registrationDTO : registrationDTOS) {
            DoctorSlotDTO doctorSlotDTO = doctorSlotService.getOne(registrationDTO.getDoctorSlotId());
            doctorSlotDTO.setIsRegistered(false);
            registrationDTO.setIsActive(false);
            mailSenderService.sendMessage(
                    registrationDTO.getId(), MAIL_SUBJECT_FOR_REGISTRATION_CANCEL, MAIL_BODY_FOR_REGISTRATION_CANCEL);
            doctorSlotService.update(doctorSlotDTO);
            registrationService.update(registrationDTO);
        }
        List<DoctorSlotDTO> doctorSlotDTOS = doctorSlotService.getAllByDoctorId(doctorId);
        for (DoctorSlotDTO doctorSlotDTO : doctorSlotDTOS) {
            doctorSlotDTO.setDeleted(true);
            doctorSlotService.update(doctorSlotDTO);
        }
        DoctorDTO doctorDTO = getOne(doctorId);
        if (doctorDTO != null) {
            doctorDTO.setDeleted(true);
            repository.save(mapper.toEntity(doctorDTO));
        }
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

    public Page<DoctorDTO> listAllDoctorsSort(Pageable pageable) {
        Page<Doctor> doctorsSortPaginated = ((DoctorRepository) repository).findDoctorsSort(pageable);
        List<DoctorDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageable, doctorsSortPaginated.getTotalElements());
    }

    public Page<DoctorDTO> listAllDoctorsSortWithDeletedFalse(Pageable pageable) {
        Page<Doctor> doctorsSortPaginated = ((DoctorRepository) repository).findAllDoctorsSortWithDeletedFalse(pageable);
        List<DoctorDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageable, doctorsSortPaginated.getTotalElements());
    }


    public void changePassword(final String uuid, final String password) {
        DoctorDTO doctorDTO = mapper.toDTO(((DoctorRepository) repository).findDoctorByChangePasswordToken(uuid));
        doctorDTO.setChangePasswordToken(null);
        doctorDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(doctorDTO);
    }

}
