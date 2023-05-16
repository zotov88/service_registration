package serviceregistration.MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import serviceregistration.dto.ClientDTO;
import serviceregistration.service.ClientService;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientMVCController {

    private final ClientService clientService;

    public ClientMVCController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        List<ClientDTO> clients = clientService.listAll();
        model.addAttribute("clients", clients);
        return "clients/list";
    }
}
