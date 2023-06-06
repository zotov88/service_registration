package serviceregistration.service.dataconstants;


import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.model.ResultMeet;

import java.util.Arrays;
import java.util.List;

public interface RegistrationTestData {
    RegistrationDTO REGISTRATION_DTO_1 = new RegistrationDTO(
            null,
            ResultMeet.SUCCESSFULLY,
            false,
            1L,
            1L
    );

    RegistrationDTO REGISTRATION_DTO_2 = new RegistrationDTO(
            null,
            ResultMeet.SUCCESSFULLY,
            false,
            2L,
            2L
    );

    RegistrationDTO REGISTRATION_DTO_3 = new RegistrationDTO(
            null,
            ResultMeet.SUCCESSFULLY,
            false,
            3L,
            3L
    );

    List<RegistrationDTO> REGISTRATION_DTO_LIST = Arrays.asList(REGISTRATION_DTO_1, REGISTRATION_DTO_2, REGISTRATION_DTO_3);

    Registration REGISTRATION_1 = new Registration(
            DoctorSlotTestData.DOCTOR_SLOT_1,
            ClientTestData.CLIENT_1,
            null,
            ResultMeet.SUCCESSFULLY,
            true
    );

    Registration REGISTRATION_2 = new Registration(
            DoctorSlotTestData.DOCTOR_SLOT_2,
            ClientTestData.CLIENT_2,
            null,
            ResultMeet.SUCCESSFULLY,
            true
    );

    Registration REGISTRATION_3 = new Registration(
            DoctorSlotTestData.DOCTOR_SLOT_3,
            ClientTestData.CLIENT_3,
            null,
            ResultMeet.SUCCESSFULLY,
            true
    );

    List<Registration> REGISTRATION_LIST = Arrays.asList(REGISTRATION_1, REGISTRATION_2, REGISTRATION_3);
}
