package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.*;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final DoctorSlotService doctorSlotService;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper,
                         UserService userService,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         RegistrationService registrationService, DoctorSlotService doctorSlotService) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.registrationService = registrationService;
        this.doctorSlotService = doctorSlotService;
    }

    public ClientDTO create(ClientDTO clientDTO) {
        int age = Period.between(LocalDate.from(clientDTO.getBirthDate()), LocalDate.now()).getYears();
        clientDTO.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        clientDTO.setRole(roleDTO);
        clientDTO.setPassword(bCryptPasswordEncoder.encode(clientDTO.getPassword()));
        if (userService.findUserByLogin(clientDTO.getLogin()) == null) {
            userService.createUser(clientDTO.getLogin(), clientDTO.getRole().getId());
        }
        return mapper.toDTO(repository.save(mapper.toEntity(clientDTO)));
    }

    public void delete(final Long id) {
        userService.deleteByLogin(getOne(id).getLogin());
        repository.deleteById(id);
    }

    public ClientDTO getClientByLogin(final String login) {
        return mapper.toDTO(((ClientRepository) repository).findClientByLoginAndIsDeletedFalse(login));
    }

    public ClientDTO getClientByEmail(final String email) {
        return mapper.toDTO(((ClientRepository) repository).findClientByEmail(email));
    }

    public void changePassword(final String uuid,
                               final String password) {
        ClientDTO clientDTO = mapper.toDTO(((ClientRepository) repository).findClientByChangePasswordToken(uuid));
        clientDTO.setChangePasswordToken(null);
        clientDTO.setPassword(password);
        update(clientDTO);
    }

    public boolean isActiveRegistrationBySpecialization(DoctorSlotDTO doctorSlot, Long clientId) {
        return ((ClientRepository)(repository)).findActiveRegistrationBySpecialization(
                doctorSlot.getDoctor().getSpecialization().getTitleSpecialization(),
                clientId) == 1L;
    }

    public boolean isActiveRegistrationByDayAndTime(DoctorSlotDTO doctorSlot, Long clientId) {
        return ((ClientRepository)(repository)).findActiveRegistrationByDay(
                doctorSlot.getDay().getId(),
                doctorSlot.getSlot().getId(),
                clientId) == 1L;
    }

    public ClientDTO getClientIdByDoctorSlot(Long doctorSlotId) {
        return mapper.toDTO(((ClientRepository)(repository)).findClientIdByDoctorSlot(doctorSlotId));
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
                clientSearchDTO.getLastName(),
                clientSearchDTO.getFirstName(),
                clientSearchDTO.getMidName(),
                pageRequest);
        List<ClientDTO> result = mapper.toDTOs(clientsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, clientsPaginated.getTotalElements());
    }

    public Page<ClientDTO> listAllWithDeletedFalse(Pageable pageRequest) {
        Page<Client> doctorsSortPaginated = ((ClientRepository) repository).findListAllWithDeletedFalse(pageRequest);
        List<ClientDTO> result = mapper.toDTOs(doctorsSortPaginated.getContent());
        return new PageImpl<>(result, pageRequest, doctorsSortPaginated.getTotalElements());
    }


}
