package serviceregistration.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ClientService extends GenericService<Client, ClientDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public ClientService(ClientRepository repository,
                         ClientMapper mapper,
                         UserService userService,
                         BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        super(repository, mapper);
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ClientDTO create(ClientDTO clientDTO) {
        int age = Period.between(LocalDate.from(clientDTO.getBirthDate()), LocalDate.now()).getYears();
        clientDTO.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        clientDTO.setRole(roleDTO);
        System.out.println("+++++++++++++++++++"+clientDTO.getPassword()+"+++++++++++++++++");
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

//    public void update(final Long id) {
//
//    }
//
//    public ClientDTO update(ClientDTO updObj) {
//        return create(updObj);
//    }

    public ClientDTO getClientByLogin(final String login) {
        return mapper.toDTO(((ClientRepository) repository).findClientByLogin(login));
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
}
