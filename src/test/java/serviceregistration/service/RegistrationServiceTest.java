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
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.Registration;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.repository.RegistrationRepository;
import serviceregistration.service.dataconstants.RegistrationTestData;
import serviceregistration.service.dataconstants.UQMImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationServiceTest extends GenericTest<Registration, RegistrationDTO> {

    private final DoctorSlotService doctorSlotService;
    private final DoctorSlotRepository doctorSlotRepository;
    private final DoctorSlotMapper doctorSlotMapper;

    public RegistrationServiceTest() {
        super();
        repository = Mockito.mock(RegistrationRepository.class);
        mapper = Mockito.mock(RegistrationMapper.class);
        service = new RegistrationService(
                (RegistrationRepository) repository,
                (RegistrationMapper) mapper,
                Mockito.mock(DoctorSlotService.class)
        );
        this.doctorSlotService = Mockito.mock(DoctorSlotService.class);
        this.doctorSlotRepository = Mockito.mock(DoctorSlotRepository.class);
        this.doctorSlotMapper = Mockito.mock(DoctorSlotMapper.class);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(RegistrationTestData.REGISTRATION_LIST);
        Mockito.when(mapper.toDTOs(RegistrationTestData.REGISTRATION_LIST)).thenReturn(RegistrationTestData.REGISTRATION_DTO_LIST);
        List<RegistrationDTO> registrationDTOS = service.listAll();
        assertEquals(RegistrationTestData.REGISTRATION_DTO_LIST.size(), repository.findAll().size());
        assertEquals(RegistrationTestData.REGISTRATION_DTO_LIST, registrationDTOS);
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(RegistrationTestData.REGISTRATION_1));
        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
        RegistrationDTO registrationDTO = service.getOne(1L);
        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(RegistrationTestData.REGISTRATION_DTO_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(repository.save(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
        RegistrationDTO registrationDTO = service.create(RegistrationTestData.REGISTRATION_DTO_1);
        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(RegistrationTestData.REGISTRATION_DTO_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(repository.save(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
        RegistrationDTO registrationDTO = service.update(RegistrationTestData.REGISTRATION_DTO_1);
        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
    }

    @Override
    protected void delete() {
    }

    @Override
    protected void restore() {

    }

//    @Test
//    @Order(5)
//    public void cancelMeet() {
//        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(RegistrationTestData.REGISTRATION_1));
//        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
//
//        Mockito.when(doctorSlotRepository.findById(RegistrationTestData.REGISTRATION_DTO_1.getDoctorSlotId()))
//                .thenReturn(Optional.of(DoctorSlotTestData.DOCTOR_SLOT_1));
//        Mockito.when(doctorSlotMapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
//
////        Mockito.when(doctorSlotService.getOne(RegistrationTestData.REGISTRATION_DTO_1.getDoctorSlotId()))
////                .thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
//
//        Mockito.when(mapper.toEntity(RegistrationTestData.REGISTRATION_DTO_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
//        Mockito.when(repository.save(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_1);
//        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
//        RegistrationDTO registrationDTO = ((RegistrationService)service).cancelMeet(1L);
//        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
//        assertFalse(RegistrationTestData.REGISTRATION_DTO_1.getIsActive());
//        assertTrue(RegistrationTestData.REGISTRATION_DTO_1.isDeleted());
//    }

    @Test
    @Order(6)
    public void getAllRegistrationsByClient() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((RegistrationRepository) repository).getAllRegistrationsByClient(1L, pageRequest))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((RegistrationService)service).getAllRegistrationsByClient(1L, pageRequest);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(7)
    public void getOnByDoctorSlotId() {
        Mockito.when(((RegistrationRepository)repository).findOnByDoctorSlotId(1L))
                .thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
        RegistrationDTO registrationDTO = ((RegistrationService)service).getOnByDoctorSlotId(1L);
        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
    }

    @Test
    @Order(8)
    public void listAllActiveRegistrationByClientId() {
        Mockito.when(((RegistrationRepository)repository).findAllActiveRegistrationByClientId(1L))
                .thenReturn(RegistrationTestData.REGISTRATION_LIST);
        Mockito.when(mapper.toDTOs(RegistrationTestData.REGISTRATION_LIST)).thenReturn(RegistrationTestData.REGISTRATION_DTO_LIST);
        List<RegistrationDTO> registrationDTOS = ((RegistrationService)service).listAllActiveRegistrationByClientId(1L);
        assertEquals(registrationDTOS, RegistrationTestData.REGISTRATION_DTO_LIST);
    }

    @Test
    @Order(9)
    public void getRegistrationsByDoctorIdWhereIsActive() {
        Mockito.when(((RegistrationRepository)repository).findRegistrationsByDoctorId(1L))
                .thenReturn(RegistrationTestData.REGISTRATION_LIST);
        Mockito.when(mapper.toDTOs(RegistrationTestData.REGISTRATION_LIST)).thenReturn(RegistrationTestData.REGISTRATION_DTO_LIST);
        List<RegistrationDTO> registrationDTOS = ((RegistrationService)service).getRegistrationsByDoctorIdWhereIsActive(1L);
        assertEquals(registrationDTOS, RegistrationTestData.REGISTRATION_DTO_LIST);
    }

    @Test
    @Order(10)
    public void getRegistrationDTOByDoctorSlotId() {
        Mockito.when(((RegistrationRepository)repository).findRegistrationDTOByDoctorSlotId(1L))
                .thenReturn(RegistrationTestData.REGISTRATION_1);
        Mockito.when(mapper.toDTO(RegistrationTestData.REGISTRATION_1)).thenReturn(RegistrationTestData.REGISTRATION_DTO_1);
        RegistrationDTO registrationDTO = ((RegistrationService)service).getRegistrationDTOByDoctorSlotId(1L);
        assertEquals(registrationDTO, RegistrationTestData.REGISTRATION_DTO_1);
    }
}
