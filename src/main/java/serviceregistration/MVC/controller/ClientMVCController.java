package serviceregistration.MVC.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import serviceregistration.constants.Errors;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.ClientSearchDTO;
import serviceregistration.service.ClientService;
import serviceregistration.service.UserService;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.Objects;
import java.util.UUID;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@RequestMapping("/clients")
public class ClientMVCController {

    private final ClientService clientService;
    private final UserService userService;
    private final PageConfig pageConfig;

    public ClientMVCController(ClientService clientService,
                               UserService userService,
                               PageConfig pageConfig) {
        this.clientService = clientService;
        this.userService = userService;
        this.pageConfig = pageConfig;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "${page.config.size.page-default}") int page,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageConfig.getPageSize());
        Page<ClientDTO> clients;
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(username)) {
            clients = clientService.listAll(pageRequest);
        } else {
            clients = clientService.listAllClientsWithDeletedFalse(pageRequest);
        }
        model.addAttribute("clients", clients);
        return "clients/list";
    }

    @PostMapping("/search")
    public String searchDoctor(@RequestParam(value = "page", defaultValue = "${page.config.size.page-default}") int page,
                               @ModelAttribute("clientSearchForm") ClientSearchDTO clientSearchDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageConfig.getPageSize());
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
        if (clientService.getClientByPolicy(clientDTO.getPolicy()) != null) {
            bindingResult.rejectValue("policy", "policy.error", "Этот полис уже существует");
            return "registration";
        }
        if (clientService.getClientByPhone(clientDTO.getPhone()) != null) {
            bindingResult.rejectValue("phone", "phone.error", "Этот телефон уже зарегистрирован");
            return "registration";
        }
        clientService.create(clientDTO);
        return "redirect:/login";
    }

    @GetMapping("remember-password")
    public String rememberPassword() {
        return "clients/rememberPassword";
    }

    @PostMapping("remember-password")
    public String rememberPassword(@ModelAttribute("changePasswordForm") ClientDTO clientDTO) {
        clientDTO = clientService.getClientByEmail(clientDTO.getEmail());
        if (Objects.isNull(clientDTO)) {
            return "Error!";
        } else {
            clientService.sendChangePasswordEmail(clientDTO);
            return "redirect:/login";
        }
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
        return "redirect:/logout";
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable Integer id,
                              Model model) throws AuthException {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.isNull(customUserDetails.getUserId())) {
            if (!ADMIN.equalsIgnoreCase(customUserDetails.getUsername())) {
                if (!id.equals(customUserDetails.getUserId()) &&
                        !customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
                    throw new AuthException(HttpStatus.FORBIDDEN + ": ");
                }
            }
        }
        model.addAttribute("user", clientService.getOne(Long.valueOf(id)));
        return "profile/viewClientProfile";
    }

    @GetMapping("/profile/update/{id}")
    public String updateProfile(@PathVariable Integer id,
                                Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId()) &&
                !customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
        }
        model.addAttribute("clientForm", clientService.getOne(Long.valueOf(id)));
        return "profile/updateProfileClient";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("clientForm") ClientDTO clientDTOFromUpdateForm,
                                BindingResult bindingResult) {
        ClientDTO userEmailDuplicated = clientService.getClientByEmail(clientDTOFromUpdateForm.getEmail());
        ClientDTO foundUser = clientService.getOne(clientDTOFromUpdateForm.getId());
        if (userEmailDuplicated != null && !Objects.equals(userEmailDuplicated.getEmail(), foundUser.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Такой email уже существует");
            return "profile/updateProfileClient";
        }
        foundUser.setFirstName(clientDTOFromUpdateForm.getFirstName());
        foundUser.setLastName(clientDTOFromUpdateForm.getLastName());
        foundUser.setMidName(clientDTOFromUpdateForm.getMidName());
        foundUser.setEmail(clientDTOFromUpdateForm.getEmail());
        foundUser.setBirthDate(clientDTOFromUpdateForm.getBirthDate());
        foundUser.setPhone(clientDTOFromUpdateForm.getPhone());
        foundUser.setAddress(clientDTOFromUpdateForm.getAddress());
        clientService.update(foundUser);
        return "redirect:/clients/profile/" + clientDTOFromUpdateForm.getId();
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
