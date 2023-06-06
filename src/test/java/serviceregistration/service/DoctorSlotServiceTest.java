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
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.service.dataconstants.ClientTestData;
import serviceregistration.service.dataconstants.DoctorSlotTestData;
import serviceregistration.service.dataconstants.DoctorTestData;
import serviceregistration.service.dataconstants.UQMImpl;

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
        Mockito.when(((DoctorSlotRepository) repository).findDSWhereRegistrationsIsActive(1L, 1L))
                .thenReturn(DoctorSlotTestData.DOCTOR_SLOT_LIST);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        Mockito.when(mapper.toEntity(DoctorSlotTestData.DOCTOR_SLOT_DTO_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(((DoctorSlotRepository) repository).findDSWhereRegistrationsIsActive(1L, 1L));
        Mockito.when(mapper.toEntity(DoctorSlotTestData.DOCTOR_SLOT_DTO_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(repository.save(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        DoctorSlotDTO doctorSlotDTO = service.create(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(doctorSlotDTO, DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(doctorSlotDTOS, DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
    }


    @Test
    @Order(6)
    @Override
    protected void restore() {
    }

    @Test
    @Order(7)
    public void getDoctorSlotByCabinetAndDay() {
        Mockito.when(((DoctorSlotRepository) repository).findFirstByCabinetIdAndDayId(1L, 1L))
                .thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(DoctorSlotTestData.DOCTOR_SLOT_DTO_1, ((DoctorSlotService)service).getDoctorSlotByCabinetAndDay(1L, 1L));
    }

    @Test
    @Order(8)
    public void getDoctorSlotByDoctorAndDay() {
        Mockito.when(((DoctorSlotRepository) repository).findFirstByDoctorIdAndDayId(1L, 1L))
                .thenReturn(DoctorSlotTestData.DOCTOR_SLOT_1);
        Mockito.when(mapper.toDTO(DoctorSlotTestData.DOCTOR_SLOT_1)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_1);
        assertEquals(DoctorSlotTestData.DOCTOR_SLOT_DTO_1, ((DoctorSlotService)service).getDoctorSlotByDoctorAndDay(1L, 1L));
    }

    @Test
    @Order(9)
    public void groupByDoctorSlot() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((DoctorSlotRepository) repository).findGroupByDoctorSlot(pageRequest))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).groupByDoctorSlot(pageRequest);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(10)
    public void findAmongGroupByDoctorSlot() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        DoctorSlotSearchDTO doctorSlotSearchDTO = new DoctorSlotSearchDTO(
                DoctorTestData.DOCTOR_1.getFirstName(),
                DoctorTestData.DOCTOR_1.getLastName(),
                DoctorTestData.DOCTOR_1.getMidName(),
                DoctorTestData.DOCTOR_1.getSpecialization().getTitleSpecialization(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getDay().getDay().toString(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getCabinet().getCabinetNumber(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getIsRegistered()
        );
        Mockito.when(((DoctorSlotRepository) repository).searchGroupByDoctorSlot(
                        doctorSlotSearchDTO.getLastName(),
                        doctorSlotSearchDTO.getFirstName(),
                        doctorSlotSearchDTO.getMidName(),
                        doctorSlotSearchDTO.getSpecialization(),
                        doctorSlotSearchDTO.getDay(),
                        pageRequest
                )).thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).findAmongGroupByDoctorSlot(pageRequest, doctorSlotSearchDTO);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(11)
    public void getActualSchedule() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((DoctorSlotRepository) repository).findActualScheduleGroup(pageRequest))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).getActualSchedule(pageRequest);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(12)
    public void getArchiveSchedule() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((DoctorSlotRepository) repository).findArchiveScheduleGroup(pageRequest))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).getArchiveSchedule(pageRequest);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(13)
    public void findAmongActualSchedule() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        DoctorSlotSearchDTO doctorSlotSearchDTO = new DoctorSlotSearchDTO(
                DoctorTestData.DOCTOR_1.getFirstName(),
                DoctorTestData.DOCTOR_1.getLastName(),
                DoctorTestData.DOCTOR_1.getMidName(),
                DoctorTestData.DOCTOR_1.getSpecialization().getTitleSpecialization(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getDay().getDay().toString(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getCabinet().getCabinetNumber(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getIsRegistered()
        );
        Mockito.when(((DoctorSlotRepository) repository).searchActualScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageRequest
        )).thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).findAmongActualSchedule(pageRequest, doctorSlotSearchDTO);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(14)
    public void findAmongAllSchedule() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        DoctorSlotSearchDTO doctorSlotSearchDTO = new DoctorSlotSearchDTO(
                DoctorTestData.DOCTOR_1.getFirstName(),
                DoctorTestData.DOCTOR_1.getLastName(),
                DoctorTestData.DOCTOR_1.getMidName(),
                DoctorTestData.DOCTOR_1.getSpecialization().getTitleSpecialization(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getDay().getDay().toString(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getCabinet().getCabinetNumber(),
                DoctorSlotTestData.DOCTOR_SLOT_1.getIsRegistered()
        );
        Mockito.when(((DoctorSlotRepository) repository).searchArchiveScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageRequest
        )).thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).findAmongAllSchedule(pageRequest, doctorSlotSearchDTO);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(15)
    public void getActualScheduleByDoctor() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((DoctorSlotRepository) repository).findActualScheduleByDoctorId(pageRequest, 1L))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).getActualScheduleByDoctor(pageRequest, 1L);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(16)
    public void getArchiveScheduleByDoctor() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Mockito.when(((DoctorSlotRepository) repository).findArchiveScheduleByDoctor(pageRequest, 1L))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).getArchiveScheduleByDoctor(pageRequest, 1L);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(16)
    public void searchArchiveScheduleByDoctor() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Day day = DoctorSlotTestData.DOCTOR_SLOT_2.getDay();
        Mockito.when(((DoctorSlotRepository) repository).findSearchArchiveScheduleByDoctor(pageRequest, 1L, day.getDay().toString()))
                .thenReturn(new PageImpl<>(UQMImpl.LIST_UQM));
        Page<UniversalQueryModel> doctorSlots = ((DoctorSlotService)service).searchArchiveScheduleByDoctor(pageRequest, 1L, day);
        assertEquals(doctorSlots.getContent(), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(17)
    public void getSlotsOneDayForDoctor() {
        Mockito.when(((DoctorSlotRepository) repository).findSlotsOneDayForDoctor(1L, 1L))
                .thenReturn(UQMImpl.LIST_UQM);
        assertEquals(((DoctorSlotService)service).getSlotsOneDayForDoctor(1L, 1L), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(18)
    public void getSlotsOneDayForClient() {
        Mockito.when(((DoctorSlotRepository) repository).findSlotsOneDayForClient(1L, 1L))
                .thenReturn(UQMImpl.LIST_UQM);
        assertEquals(((DoctorSlotService)service).getSlotsOneDayForClient(1L, 1L), UQMImpl.LIST_UQM);
    }

    @Test
    @Order(19)
    public void getAllByDoctorId() {
        Mockito.when(((DoctorSlotRepository) repository).findAllByDoctorId(1L)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_LIST);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        assertEquals(((DoctorSlotService)service).getAllByDoctorId(1L), DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
    }

    @Test
    @Order(20)
    public void getCabinetByDoctorIdAndDayId() {
        Mockito.when(((DoctorSlotRepository) repository).findCabinetByDoctorIdAndDayId(1L, 1L)).thenReturn(1);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        assertEquals(((DoctorSlotService)service).getCabinetByDoctorIdAndDayId(1L, 1L), 1);
    }

    @Test
    @Order(21)
    public void listAllActiveDoctorSlotsByClientId() {
        Mockito.when(((DoctorSlotRepository) repository).findAllActiveDoctorSlotsByClientId(1L))
                .thenReturn(DoctorSlotTestData.DOCTOR_SLOT_LIST);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        assertEquals(((DoctorSlotService)service).listAllActiveDoctorSlotsByClientId(1L), DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
    }

    @Test
    @Order(22)
    public void getPosiblyCancelMeet() {
        List<Long> longList = List.of(1L);
        Mockito.when(((DoctorSlotRepository) repository).findPosiblyCancelMeet(1L, 1L)).thenReturn(longList);
        Mockito.when(mapper.toDTOs(DoctorSlotTestData.DOCTOR_SLOT_LIST)).thenReturn(DoctorSlotTestData.DOCTOR_SLOT_DTO_LIST);
        assertEquals(((DoctorSlotService)service).getPosiblyCancelMeet(1L, 1L), longList);
    }
}
