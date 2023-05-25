package serviceregistration.MVC.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.ClientDTO;
import serviceregistration.service.ClientService;
import serviceregistration.service.UserService;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/clients")
public class ClientMVCController {

    private final ClientService clientService;
    private final UserService userService;

    public ClientMVCController(ClientService clientService,
                               UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("clientForm", new ClientDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("clientForm") ClientDTO clientDTO,
                               BindingResult bindingResult) {
        String login = clientDTO.getLogin().toLowerCase();
        clientDTO.setLogin(login);
        if (login.equalsIgnoreCase(ADMIN) ||
                (userService.findUserByLogin(login) != null && userService.findUserByLogin(login).getLogin().equals(login))) {
            bindingResult.rejectValue("login", "login.error", "Этот логин уже существует");
            return "registration";
        }
        if (clientService.getClientByEmail(clientDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.error", "Этот email уже существует");
            return "registration";
        }
        clientService.create(clientDTO);
        return "redirect:login";
    }

    @GetMapping("/list")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<ClientDTO> clients = clientService.listAll(pageRequest);
        model.addAttribute("clients", clients);
        return "clients/list";
    }

//    @GetMapping("/profile")
//    public String profile() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication auth = context.getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
//        return "clients/profile/";
//    }
}
