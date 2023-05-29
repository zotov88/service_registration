package serviceregistration.dto.querymodel;

public interface DoctorSlotForSchedule extends DoctorName {

    Integer getDoctorId();
    Integer getDayId();
    String getSpecialization();
    String getDay();
    String getCabinet();
    Boolean getIsDeleted();

}
