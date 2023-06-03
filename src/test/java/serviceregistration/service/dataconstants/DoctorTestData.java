package serviceregistration.service.dataconstants;


import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.Doctor;
import serviceregistration.model.Role;
import serviceregistration.model.Specialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DoctorTestData {
    DoctorDTO DOCTOR_DTO_1 = new DoctorDTO(
            "login2",
            "password2",
            "firstName2",
            "midName2",
            "lastName2",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            new ArrayList<>()
    );
    
    DoctorDTO DOCTOR_DTO_2 = new DoctorDTO(
            "login1",
            "password1",
            "firstName1",
            "midName1",
            "lastName1",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            new ArrayList<>()
    );
    
    DoctorDTO DOCTOR_DTO_3 = new DoctorDTO(
            "login3",
            "password3",
            "firstName3",
            "midName3",
            "lastName3",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            new ArrayList<>()
    );
    
    List<DoctorDTO> DOCTOR_DTO_LIST = Arrays.asList(DOCTOR_DTO_1, DOCTOR_DTO_2, DOCTOR_DTO_3);

    Doctor DOCTOR_1 = new Doctor(
            "login1",
            "password1",
            "firstName1",
            "midName1",
            "lastName1",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            null
    );
    
    Doctor DOCTOR_2 = new Doctor(
            "login2",
            "password2",
            "firstName2",
            "midName2",
            "lastName2",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            null
    );
    
    Doctor DOCTOR_3 = new Doctor(
            "login3",
            "password3",
            "firstName3",
            "midName3",
            "lastName3",
            new Specialization(1L, "Терапевт", "Терапевт"),
            new Role(2L, "DOCTOR", "doctor"),
            new ArrayList<>()
    );
    
    List<Doctor> DOCTOR_LIST = Arrays.asList(DOCTOR_1, DOCTOR_2, DOCTOR_3);
}
