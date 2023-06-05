package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.service.dataconstants.ClientTestData;
import serviceregistration.service.dataconstants.DoctorSlotTestData;
import serviceregistration.service.dataconstants.DoctorTestData;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorSlotServiceTest extends GenericTest<DoctorSlot, DoctorSlotDTO> {

    public DoctorSlotServiceTest() {
        super();
        repository = Mockito.mock(DoctorSlotRepository.class);
        mapper = Mockito.mock(DoctorSlotMapper.class);
        service = new DoctorSlotService(
                (DoctorSlotRepository) repository,
                (DoctorSlotMapper) mapper,
                Mockito.mock(RegistrationService.class),
                Mockito.mock(MailSenderService.class)
        );
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_LIST);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        List<DoctorSlotDTO> doctorSlotDTOS = service.listAll();
        assertEquals(ClientTestData.CLIENT_LIST.size(), repository.findAll().size());
        assertEquals(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST.size(), doctorSlotDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DoctorSlotTestData.DOCTOR_SLOT_1));
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        DoctorSlotDTO doctorSlotDTO = service.getOne(1L);
        assertEquals(doctorSlotDTO, DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(DoctorSlotTestData.DOCTOR_SLOT_DTO_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(repository.save(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        DoctorSlotDTO doctorSlotDTO = service.create(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(doctorSlotDTO, DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        DoctorSlotTestData.DOCTOR_SLOT_DTO_1.setDoctor(DoctorTestData.DOCTOR_2);
        Mockito.when(mapper.toEntity(DoctorSlotTestData.DOCTOR_SLOT_DTO_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(repository.save(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        DoctorSlotDTO doctorSlotDTO = service.create(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(doctorSlotDTO, DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(doctorSlotDTO.getDoctor(), DoctorTestData.DOCTOR_2);
    }

    @Test
    @Order(5)
    @Override
    protected void delete() {

    }

    @Test
    @Order(6)
    @Override
    protected void restore() {

    }
}
