package serviceregistration.MVC.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.model.Role;
import serviceregistration.model.Specialization;
import serviceregistration.service.DoctorService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@Rollback(value = false)
public class MVCDoctorControllerTest extends CommonTestMVC {

    @Autowired
    private DoctorService doctorService;

    private final DoctorDTO doctorDTO = new DoctorDTO("login", "p", "я", "я", "я", new Specialization(1L, "t", "s"),
            new Role(1L, "r", "d"), null, new ArrayList<>());

    @Override
    @Test
    @Order(0)
    @DisplayName("Просмотр всех врачей через MVC контроллер")
    @WithAnonymousUser
    protected void listAll() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("doctors/list"))
                .andExpect(model().attributeExists("doctors"))
                .andExpect(model().attributeExists("specializations"))
                .andReturn();
    }

    @Test
    @Order(1)
    @DisplayName("Не даст создать врача из-за конфликта логинов через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Override
    protected void createObject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/doctors/addDoctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("doctorForm", doctorDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctors"))
                .andExpect(redirectedUrlTemplate("/doctors"))
                .andExpect(redirectedUrl("/doctors"));
    }

    @Test
    @Order(2)
    @DisplayName("Создание врача через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObjectWithLoginYetExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/doctors/addDoctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("doctorForm", doctorDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("doctors/addDoctor"));
    }

    @Test
    @Override
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @DisplayName("Просмотр всех врачей через MVC контроллер")
    @Order(3)
    protected void updateObject() throws Exception {
        DoctorDTO doctor = doctorService.getOne(1L);
        PageRequest pageRequest = PageRequest.of(0, 5);
        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO();
        doctorSearchDTO.setFirstName(doctor.getFirstName());
        DoctorDTO foundDoctorForUpdate = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest).getContent().get(0);
        foundDoctorForUpdate.setFirstName("Валерия");
        mvc.perform(MockMvcRequestBuilders.post("/doctors/profile/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("clientForm", foundDoctorForUpdate)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctors/profile/1"))
                .andExpect(redirectedUrl("/doctors/profile/1"));
    }

    @Test
    @Override
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @DisplayName("Удаление врача через MVC контроллер")
    @Order(4)
    protected void deleteObject() throws Exception {
        DoctorDTO doctor = doctorService.getOne(1L);
        PageRequest pageRequest = PageRequest.of(0, 5);
        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO();
        doctorSearchDTO.setFirstName(doctor.getFirstName());
        DoctorDTO foundDoctorForDelete = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest).getContent().get(0);
        foundDoctorForDelete.setDeleted(true);
        mvc.perform(MockMvcRequestBuilders.get("/doctors/softdelete/{id}", foundDoctorForDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctors"))
                .andExpect(redirectedUrl("/doctors"));
        DoctorDTO deletedDoctor = doctorService.getDoctorByLogin(foundDoctorForDelete.getLogin());
        assertTrue(deletedDoctor.isDeleted());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @DisplayName("Восстановление врача через MVC контроллер")
    @Order(5)
    public void restoreObject() throws Exception {
        DoctorDTO doctor = doctorService.getOne(1L);
        PageRequest pageRequest = PageRequest.of(0, 5);
        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO();
        doctorSearchDTO.setFirstName(doctor.getFirstName());
        DoctorDTO foundDoctorForDelete = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest).getContent().get(0);
        foundDoctorForDelete.setDeleted(false);
        mvc.perform(MockMvcRequestBuilders.get("/doctors/restore/{doctorId}", foundDoctorForDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctors"))
                .andExpect(redirectedUrl("/doctors"));
        DoctorDTO deletedDoctor = doctorService.getDoctorByLogin(foundDoctorForDelete.getLogin());
        assertFalse(deletedDoctor.isDeleted());
    }

    @Test
    @Order(6)
    @DisplayName("Просмотр врача через MVC контроллер")
    @WithAnonymousUser
    public void viewDoctor() throws Exception {
        DoctorDTO doctor = doctorService.getOne(1L);
        PageRequest pageRequest = PageRequest.of(0, 5);
        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO();
        doctorSearchDTO.setFirstName(doctor.getFirstName());
        DoctorDTO foundDoctor = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest).getContent().get(0);
        mvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}", foundDoctor.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("doctor"))
                .andExpect(view().name("doctors/viewDoctor"));
    }

//    @Test
//    @Order(7)
//    @DisplayName("Просмотр профиля врача через MVC контроллер")
//    @WithMockUser(username = "aad", roles = "DOCTOR", password = "d")
//    public void viewDoctorProfile() throws Exception {
//        DoctorDTO doctor = doctorService.getOne(1L);
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO();
//        doctorSearchDTO.setFirstName(doctor.getFirstName());
//        DoctorDTO foundDoctor = doctorService.searchDoctorsSort(doctorSearchDTO, pageRequest).getContent().get(0);
//        mvc.perform(MockMvcRequestBuilders.get("/doctors/profile/{doctorId}", foundDoctor.getId())
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(model().attributeExists("doctor"));
//
//    }

}
