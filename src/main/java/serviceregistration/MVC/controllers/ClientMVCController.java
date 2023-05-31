package serviceregistration.MVC.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.ClientSearchDTO;
import serviceregistration.service.ClientService;
import serviceregistration.service.UserService;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.UUID;

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

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<ClientDTO> clients;
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(username)) {
            clients = clientService.listAll(pageRequest);
        } else {
            clients = clientService.listAllWithDeletedFalse(pageRequest);
        }
        model.addAttribute("clients", clients);
        return "clients/list";
    }

    @PostMapping("/search")
    public String searchDoctor(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int pageSize,
                               @ModelAttribute("clientSearchForm") ClientSearchDTO clientSearchDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<ClientDTO> clients;
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(username)) {
            clients = clientService.findClients(clientSearchDTO, pageRequest);
        } else {
            clients = clientService.findClientsWithDeletedFalse(clientSearchDTO, pageRequest);
        }
        model.addAttribute("clients", clients);
        return "clients/list";
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

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable Integer id,
                              Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (!Objects.isNull(customUserDetails.getUserId())) {
//            if (!ADMIN.equalsIgnoreCase(customUserDetails.getUsername())) {
//                if (!id.equals(customUserDetails.getUserId())) {
//                    throw new AuthException(HttpStatus.FORBIDDEN + ": " );
//                }
//            }
//        }
        model.addAttribute("user", clientService.getOne(Long.valueOf(id)));
        return "profile/viewProfile";
    }

    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 Model model) {
        model.addAttribute("uuid", uuid);
        return "clients/changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 @ModelAttribute("changePasswordForm") ClientDTO clientDTO) {
        clientService.changePassword(uuid, clientDTO.getPassword());
        return "redirect:/login";
    }

    @GetMapping("/change-password/client")
    public String changePassword(Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientDTO clientDTO = clientService.getOne(Long.valueOf(customUserDetails.getUserId()));
        UUID uuid = UUID.randomUUID();
        clientDTO.setChangePasswordToken(uuid.toString());
        clientService.update(clientDTO);
        model.addAttribute("uuid", uuid);
        return "clients/changePassword";
    }

    @GetMapping("/softdelete/{clientId}")
    public String softDelete(@PathVariable Long clientId) {
        clientService.softDelete(clientId);
        return "redirect:/clients";
    }

    @GetMapping("/restore/{clientId}")
    public String restore(@PathVariable Long clientId) {
        clientService.restore(clientId);
        return "redirect:/clients";
    }
}
