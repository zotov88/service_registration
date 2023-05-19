package serviceregistration.repository;

import serviceregistration.model.Doctor;

public interface DoctorRepository extends GenericRepository<Doctor> {

    Doctor findDoctorByLogin(String login);
}
