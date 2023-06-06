package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.constants.MailConstants;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.ClientSearchDTO;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.model.Role;
import serviceregistration.repository.ClientRepository;
import serviceregistration.utils.MailUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;
    private final JavaMailSender javaMailSender;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper,
                         UserService userService,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         RegistrationService registrationService,
                         DoctorSlotService doctorSlotService,
                         JavaMailSender javaMailSender) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
        this.javaMailSender = javaMailSender;
    }

    public ClientDTO create(ClientDTO clientDTO) {
        int age = Period.between(LocalDate.from(clientDTO.getBirthDate()), LocalDate.now()).getYears();
        clientDTO.setAge(age);
        Role role = new Role();
        role.setId(1L);
        clientDTO.setRole(role);
        clientDTO.setPassword(bCryptPasswordEncoder.encode(clientDTO.getPassword()));
        if (userService.findUserByLogin(clientDTO.getLogin()) == null) {
            userService.createUser(clientDTO.getLogin(), clientDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(clientDTO)));
    }

    public ClientDTO update(ClientDTO updObj) {
        return mapper.toDTO(repository.save(mapper.toEntity(updObj)));
    }

    public void delete(final Long id) {
        userService.deleteByLogin(getOne(id).getLogin());
        repository.deleteById(id);
    }

    public ClientDTO getClientByLogin(final String login) {
        return mapper.toDTO(((ClientRepository) repository).findClientByLoginAndIsDeletedFalse(login));
    }

    public ClientDTO getClientByEmail(final String email) {
        return mapper.toDTO(((ClientRepository) repository).findClientByEmailAndIsDeletedFalse(email));
    }

    public ClientDTO getClientByPolicy(Long policy) {
        return mapper.toDTO(((ClientRepository) repository).findClientByPolicyAndIsDeletedFalse(policy));
    }

    public ClientDTO getClientByPhone(Long phone) {
        return mapper.toDTO(((ClientRepository) repository).findClientByPhoneAndIsDeletedFalse(phone));
    }

    public void changePassword(final String uuid,
                               final String password) {
        ClientDTO clientDTO = mapper.toDTO(((ClientRepository) repository).findClientByChangePasswordToken(uuid));
        clientDTO.setChangePasswordToken(null);
        clientDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(clientDTO);
    }

    public boolean isActiveRegistrationBySpecialization(DoctorSlotDTO doctorSlot, Long clientId) {
        return ((ClientRepository) (repository)).findActiveRegistrationBySpecialization(
                doctorSlot.getDoctor().getSpecialization().getTitleSpecialization(),
                clientId) == 1L;
    }

    public boolean isActiveRegistrationByDayAndTime(DoctorSlotDTO doctorSlot, Long clientId) {
        return ((ClientRepository) (repository)).findActiveRegistrationByDay(
                doctorSlot.getDay().getId(),
                doctorSlot.getSlot().getId(),
                clientId) == 1L;
    }

    public ClientDTO getClientIdByDoctorSlot(Long doctorSlotId) {
        return mapper.toDTO(((ClientRepository) (repository)).findClientIdByDoctorSlot(doctorSlotId));
    }

    public void softDelete(Long clientId) {
        ClientDTO clientDTO = getOne(clientId);
        List<RegistrationDTO> registrations = registrationService.listAllActiveRegistrationByClientId(clientDTO.getId());
        List<DoctorSlotDTO> doctorSlots = doctorSlotService.listAllActiveDoctorSlotsByClientId(clientDTO.getId());
        for (RegistrationDTO registrationDTO : registrations) {
            registrationDTO.setDeleted(true);
            registrationDTO.setIsActive(false);
            registrationService.update(registrationDTO);
        }
        for (DoctorSlotDTO doctorSlot : doctorSlots) {
            doctorSlot.setIsRegistered(false);
            doctorSlotService.update(doctorSlot);
        }
        clientDTO.setDeleted(true);
        repository.save(mapper.toEntity(clientDTO));
    }

    public void restore(Long clientId) {
        ClientDTO clientDTO = getOne(clientId);
        clientDTO.setDeleted(false);
        repository.save(mapper.toEntity(clientDTO));
    }

    public Page<ClientDTO> findClients(ClientSearchDTO clientSearchDTO,
                                       Pageable pageRequest) {
        Page<Client> clientsPaginated = ((ClientRepository) repository).searchClients(
                clientSearchDTO.getLastName(),
                clientSearchDTO.getFirstName(),
                clientSearchDTO.getMidName(),
                pageRequest);
        List<ClientDTO> result = mapper.toDTOs(clientsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, clientsPaginated.getTotalElements());
    }

    public Page<ClientDTO> findClientsWithDeletedFalse(ClientSearchDTO clientSearchDTO,
                                                       PageRequest pageRequest) {
        Page<Client> clientsPaginated = ((ClientRepository) repository).searchClientsWithDeletedFalse(
                clientSearchDTO.getFirstName(),
                clientSearchDTO.getLastName(),
                clientSearchDTO.getMidName(),
                pageRequest);
        List<ClientDTO> result = mapper.toDTOs(clientsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, clientsPaginated.getTotalElements());
    }

    public Page<ClientDTO> listAll(Pageable pageRequest) {
        Page<Client> doctorsSortPaginated = ((ClientRepository) repository).findListAll(pageRequest);
        List<ClientDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsSortPaginated.getTotalElements());
    }

    public Page<ClientDTO> listAllClientsWithDeletedFalse(Pageable pageRequest) {
        Page<Client> doctorsSortPaginated = ((ClientRepository) repository).findListAllClientsWithDeletedFalse(pageRequest);
        List<ClientDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsSortPaginated.getTotalElements());
    }

    public void sendChangePasswordEmail(final ClientDTO clientDTO) {
        UUID uuid = UUID.randomUUID();
        clientDTO.setChangePasswordToken(uuid.toString());
        update(clientDTO);
        SimpleMailMessage mailMessage = MailUtils.createMailMessage(
                clientDTO.getEmail(),
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid);
        javaMailSender.send(mailMessage);
    }

}
