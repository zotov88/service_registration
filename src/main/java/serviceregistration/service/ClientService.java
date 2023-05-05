package serviceregistration.service;

import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.RoleDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;

public class ClientService extends GenericService<Client, ClientDTO> {

    public ClientService(ClientRepository repository,
                         ClientMapper mapper) {
        super(repository, mapper);
    }

    public ClientDTO create(ClientDTO newObj) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObj.setRole(roleDTO);
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }
}
