package serviceregistration.service.dataconstants;

import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.Cabinet;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.Slot;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DoctorSlotTestData {
    DoctorSlotDTO DOCTOR_SLOT_DTO_1 = new DoctorSlotDTO (
            DoctorTestData.DOCTOR_1,
            new Day(1L, LocalDate.ofYearDay(2023, 2), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(1L, 1, "cabinet", new ArrayList<>()),
            false,
            new ArrayList<>()
    );
    
    DoctorSlotDTO DOCTOR_SLOT_DTO_2 = new DoctorSlotDTO(
            DoctorTestData.DOCTOR_2,
            new Day(2L, LocalDate.ofYearDay(2023, 154), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(2L, 2, "cabinet", new ArrayList<>()),
            false,
            new ArrayList<>()
    );

    DoctorSlotDTO DOCTOR_SLOT_DTO_3 = new DoctorSlotDTO(
            DoctorTestData.DOCTOR_3,
            new Day(3L, LocalDate.ofYearDay(2023, 200), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(3L, 3, "cabinet", new ArrayList<>()),
            false,
            new ArrayList<>()
    );
    
    List<DoctorSlotDTO> DOCTOR_SLOT_DTO_LIST = Arrays.asList(DOCTOR_SLOT_DTO_1, DOCTOR_SLOT_DTO_2, DOCTOR_SLOT_DTO_3);

    DoctorSlot DOCTOR_SLOT_1 = new DoctorSlot(
            DoctorTestData.DOCTOR_1,
            new Day(1L, LocalDate.ofYearDay(2023, 2), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(1L, 1, "cabinet", new ArrayList<>()),
            new ArrayList<>(),
            false
    );
    
    DoctorSlot DOCTOR_SLOT_2 = new DoctorSlot(
            DoctorTestData.DOCTOR_2,
            new Day(2L, LocalDate.ofYearDay(2023, 154), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(2L, 2, "cabinet", new ArrayList<>()),
            new ArrayList<>(),
            false
    );

    DoctorSlot DOCTOR_SLOT_3 = new DoctorSlot(
            DoctorTestData.DOCTOR_3,
            new Day(3L, LocalDate.ofYearDay(2023, 200), new ArrayList<>()),
            new Slot(1L, Time.valueOf(LocalTime.now()), new ArrayList<>()),
            new Cabinet(3L, 3, "cabinet", new ArrayList<>()),
            new ArrayList<>(),
            false
    );
    
    List<DoctorSlot> DOCTOR_SLOT_LIST = Arrays.asList(DOCTOR_SLOT_1, DOCTOR_SLOT_2, DOCTOR_SLOT_3);
}
