package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.GenericRepository;

@RestController
@RequestMapping("/clients")
@Tag(name = "Клиенты", description = "Контроллер для работы с клиентами поликлиники")
public class ClientController
        extends GenericController<Client> {

    private final ClientRepository clientRepository;

    public ClientController(GenericRepository<Client> genericRepository, ClientRepository clientRepository) {
        super(genericRepository);
        this.clientRepository = clientRepository;
    }
}
