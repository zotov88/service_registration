package serviceregistration.dto.querymodel;

public interface DoctorDay extends DoctorName {

    Long getDoctorId();

    Long getDayId();

    String getSpecialization();

    String getDay();

}
