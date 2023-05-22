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

    public ClientService(ClientRepository repository,
                         ClientMapper mapper,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ClientDTO create(ClientDTO newObj) {
        int age = Period.between(LocalDate.from(newObj.getBirthDate()), LocalDate.now()).getYears();
//        System.out.println(ChronoUnit.YEARS.between(localDate, LocalDate.now())); //returns 11
        newObj.setAge(age);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        newObj.setRole(roleDTO);
        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    public ClientDTO getClientByLogin(final String login) {
        return mapper.toDTO(((ClientRepository)repository).findClientByLogin(login));
    }

    public ClientDTO getClientByEmail(final String email) {
        return mapper.toDTO(((ClientRepository)repository).findClientByEmail(email));
    }
}
