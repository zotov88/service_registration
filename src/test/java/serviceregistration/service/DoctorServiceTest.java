package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.dto.DoctorSearchDTO;
import serviceregistration.mapper.DoctorMapper;
import serviceregistration.model.Doctor;
import serviceregistration.repository.DoctorRepository;
import serviceregistration.service.dataconstants.DoctorTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorServiceTest extends GenericTest<Doctor, DoctorDTO> {

    public DoctorServiceTest() {
        super();
        repository = Mockito.mock(DoctorRepository.class);
        mapper = Mockito.mock(DoctorMapper.class);
        service = new DoctorService(
                (DoctorRepository) repository,
                (DoctorMapper) mapper,
                Mockito.mock(UserService.class),
                Mockito.mock(BCryptPasswordEncoder.class),
                Mockito.mock(DoctorSlotService.class),
                Mockito.mock(RegistrationService.class),
                Mockito.mock(MailSenderService.class));
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(DoctorTestData.DOCTOR_LIST);
        Mockito.when(mapper.toDTOs(DoctorTestData.DOCTOR_LIST)).thenReturn(DoctorTestData.DOCTOR_DTO_LIST);
        List<DoctorDTO> doctorDTOS = service.listAll();
        assertEquals(DoctorTestData.DOCTOR_LIST.size(), repository.findAll().size());
        assertEquals(DoctorTestData.DOCTOR_DTO_LIST.size(), doctorDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DoctorTestData.DOCTOR_1));
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_DTO_1);
        DoctorDTO doctorDTO = service.getOne(1L);
        assertEquals(doctorDTO, DoctorTestData.DOCTOR_DTO_1);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(DoctorTestData.DOCTOR_DTO_2)).thenReturn(DoctorTestData.DOCTOR_2);
        Mockito.when(repository.save(DoctorTestData.DOCTOR_2)).thenReturn(DoctorTestData.DOCTOR_2);
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_2)).thenReturn(DoctorTestData.DOCTOR_DTO_2);
        DoctorDTO doctorDTO = service.create(DoctorTestData.DOCTOR_DTO_2);
        assertEquals(doctorDTO, DoctorTestData.DOCTOR_DTO_2);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(DoctorTestData.DOCTOR_DTO_2)).thenReturn(DoctorTestData.DOCTOR_2);
        Mockito.when(repository.save(DoctorTestData.DOCTOR_2)).thenReturn(DoctorTestData.DOCTOR_2);
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_2)).thenReturn(DoctorTestData.DOCTOR_DTO_2);
        DoctorDTO doctorDTO = service.create(DoctorTestData.DOCTOR_DTO_2);
        assertEquals(doctorDTO, DoctorTestData.DOCTOR_DTO_2);
    }

    @Test
    @Order(5)
    @Override
    protected void delete() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DoctorTestData.DOCTOR_1));
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_DTO_1);
        Mockito.when(service.getOne(1L)).thenReturn(DoctorTestData.DOCTOR_DTO_1);
        Mockito.when(mapper.toEntity(DoctorTestData.DOCTOR_DTO_1)).thenReturn(DoctorTestData.DOCTOR_1);
        Mockito.when(repository.save(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_1);
        ((DoctorService)service).softDelete(1L);
        assertTrue(DoctorTestData.DOCTOR_DTO_1.isDeleted());
    }

    @Test
    @Order(6)
    @Override
    protected void restore() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DoctorTestData.DOCTOR_1));
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_DTO_1);
        Mockito.when(mapper.toEntity(DoctorTestData.DOCTOR_DTO_1)).thenReturn(DoctorTestData.DOCTOR_1);
        Mockito.when(repository.save(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_1);
        ((DoctorService)service).restore(1L);
        assertFalse(DoctorTestData.DOCTOR_DTO_1.isDeleted());
    }

    @Test
    @Order(7)
    public void searchDoctors() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        DoctorSearchDTO doctorSearchDTO = new DoctorSearchDTO(
                DoctorTestData.DOCTOR_1.getLastName(),
                DoctorTestData.DOCTOR_1.getFirstName(),
                DoctorTestData.DOCTOR_1.getMidName(),
                DoctorTestData.DOCTOR_1.getSpecialization().getTitleSpecialization()
        );
        Mockito.when(((DoctorRepository)repository).findSearchDoctorsSort(
                doctorSearchDTO.getLastName(),
                doctorSearchDTO.getFirstName(),
                doctorSearchDTO.getMidName(),
                doctorSearchDTO.getSpecialization(),
                pageRequest
        )).thenReturn(new PageImpl<>(DoctorTestData.DOCTOR_LIST));

        Mockito.when(mapper.toDTOs(DoctorTestData.DOCTOR_LIST)).thenReturn(DoctorTestData.DOCTOR_DTO_LIST);

        Page<DoctorDTO> doctorDTOPage = ((DoctorService) service).searchDoctorsSort(doctorSearchDTO, pageRequest);
        assertEquals(DoctorTestData.DOCTOR_DTO_LIST, doctorDTOPage.getContent());
    }

    @Test
    @Order(8)
    public void searchDoctorsWithIsDeletedFalse() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        DoctorTestData.DOCTOR_3.setDeleted(true);
        List<Doctor> doctors = new ArrayList<>();
        for (Doctor doctor : DoctorTestData.DOCTOR_LIST) {
            if (!doctor.isDeleted()) {
                doctors.add(doctor);
            }
        }
        Mockito.when(((DoctorRepository)repository).findAllDoctorsSortWithDeletedFalse(pageRequest))
                .thenReturn(new PageImpl<>(doctors));
        Mockito.when(mapper.toDTOs(doctors)).thenReturn(
            DoctorTestData.DOCTOR_DTO_LIST.stream().filter(Predicate.not(DoctorDTO::isDeleted)).toList());
        Page<DoctorDTO> doctorDTOS = ((DoctorService) service).listAllDoctorsSortWithDeletedFalse(pageRequest);
        assertEquals(DoctorTestData.DOCTOR_DTO_LIST, doctorDTOS.getContent());

    }

    @Test
    @Order(9)
    public void findByLogin() {
        Mockito.when(((DoctorRepository) repository).findDoctorByLogin("login1")).thenReturn(DoctorTestData.DOCTOR_1);
        Mockito.when(mapper.toDTO(DoctorTestData.DOCTOR_1)).thenReturn(DoctorTestData.DOCTOR_DTO_1);
        assertEquals(((DoctorService)service).getDoctorByLogin("login1"), DoctorTestData.DOCTOR_DTO_1);
    }
}
