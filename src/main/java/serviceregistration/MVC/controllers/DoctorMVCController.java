package serviceregistration.MVC.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.websocket.server.PathParam;
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
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.service.DoctorService;
import serviceregistration.service.SpecializationService;
import serviceregistration.service.userdetails.CustomUserDetails;

import java.util.Objects;
import java.util.UUID;

import static serviceregistration.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/doctors")
public class DoctorMVCController {

    private final DoctorService doctorService;
    private final SpecializationService specializationService;

    public DoctorMVCController(DoctorService doctorService,
                               SpecializationService specializationService) {
        this.doctorService = doctorService;
        this.specializationService = specializationService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorDTO> doctors;
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(username)) {
            doctors = doctorService.listAllDoctorsSort(pageRequest);
        } else {
            doctors = doctorService.listAllDoctorsSortWithDeletedFalse(pageRequest);
        }
        model.addAttribute("doctors", doctors);
        model.addAttribute("specializations", specializationService.listAll());
        return "doctors/list";
    }

    @PostMapping("/search")
    public String searchDoctor(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "10") int pageSize,
                               @ModelAttribute("doctorSearchForm") DoctorSearchDTO doctorSearchDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<DoctorDTO> doctors;
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(username)) {
            doctors = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest);
        } else {
            doctors = doctorService.searchDoctorsSortWithDeletedFalse(doctorSearchDTO, pageRequest);
        }
        model.addAttribute("doctors", doctors);
        model.addAttribute("specializations", specializationService.listAll());
        return "doctors/list";
    }

    @GetMapping("/{id}")
    public String viewDoctor(@PathVariable Long id,
                             Model model) {
        model.addAttribute("doctor", doctorService.getOne(id));
        return "doctors/viewDoctor";
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable Integer id,
                              Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.isNull(customUserDetails.getUserId())) {
            if (!ADMIN.equalsIgnoreCase(customUserDetails.getUsername())) {
                if (!id.equals(customUserDetails.getUserId()) &&
                        !customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
                    throw new AuthException(HttpStatus.FORBIDDEN + ": ");
                }
            }
        }
        model.addAttribute("doctor", doctorService.getOne(Long.valueOf(id)));
        return "profile/viewDoctorProfile";
    }

    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 Model model) {
        model.addAttribute("uuid", uuid);
        return "doctors/changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 @ModelAttribute("changePasswordForm") ClientDTO clientDTO) {
        doctorService.changePassword(uuid, clientDTO.getPassword());
        return "redirect:/logout";
    }

    @GetMapping("/change-password/doctor")
    public String changePassword(Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DoctorDTO doctorDTO = doctorService.getOne(Long.valueOf(customUserDetails.getUserId()));
        UUID uuid = UUID.randomUUID();
        doctorDTO.setChangePasswordToken(uuid.toString());
        doctorService.update(doctorDTO);
        model.addAttribute("uuid", uuid);
        return "doctors/changePassword";
    }

    @GetMapping("/profile/update/{id}")
    public String updateProfile(@PathVariable Integer id,
                                Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId()) &&
                !customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
        }
        model.addAttribute("doctorForm", doctorService.getOne(Long.valueOf(id)));
        return "profile/updateProfileDoctor";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("clientForm") DoctorDTO doctorDTOFromUpdateForm) {
        DoctorDTO foundDoctor = doctorService.getOne(doctorDTOFromUpdateForm.getId());
        foundDoctor.setFirstName(doctorDTOFromUpdateForm.getFirstName());
        foundDoctor.setLastName(doctorDTOFromUpdateForm.getLastName());
        foundDoctor.setMidName(doctorDTOFromUpdateForm.getMidName() == null ? "" : doctorDTOFromUpdateForm.getMidName());
        doctorService.update(foundDoctor);
        return "redirect:/doctors/profile/" + doctorDTOFromUpdateForm.getId();
    }

    @GetMapping("/addDoctor")
    public String addDoctor(Model model) {
        model.addAttribute("specializations", specializationService.listAll());
        model.addAttribute("doctorForm", new DoctorDTO());
        return "doctors/addDoctor";
    }



    @PostMapping("/addDoctor")
    public String addDoctor(@ModelAttribute("doctorForm") DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model) {
        String login = doctorDTO.getLogin().toLowerCase();
        doctorDTO.setLogin(login);
        if (doctorDTO.getLogin().equalsIgnoreCase(ADMIN) ||
                doctorService.getDoctorByLogin(login) != null && doctorService.getDoctorByLogin(login).getLogin().equals(login)) {
            bindingResult.rejectValue("login", "error.login", "Этот логин уже существует");
            model.addAttribute("specializations", specializationService.listAll());
            return "doctors/addDoctor";
        }
        doctorService.create(doctorDTO);
        return "redirect:/doctors";
    }

    @GetMapping("/softdelete/{doctorId}")
    public String softDelete(@PathVariable Long doctorId) {
        doctorService.softDelete(doctorId);
        return "redirect:/doctors";
    }

    @GetMapping("/restore/{doctorId}")
    public String restore(@PathVariable Long doctorId) {
        doctorService.restore(doctorId);
        return "redirect:/doctors";
    }
}
