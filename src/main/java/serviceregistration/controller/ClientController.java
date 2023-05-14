package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.ClientDTO;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;
import serviceregistration.service.ClientService;
import serviceregistration.service.GenericService;

@RestController
@RequestMapping("/clients")
@Tag(name = "Клиенты", description = "Контроллер для работы с клиентами поликлиники")
public class ClientController extends GenericController<Client, ClientDTO> {

    private final ClientService clientService;

    public ClientController(ClientService service) {
        super(service);
        this.clientService = service;
    }
}
