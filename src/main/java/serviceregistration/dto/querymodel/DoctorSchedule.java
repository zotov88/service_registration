package serviceregistration.dto.querymodel;

public interface DoctorSchedule {

    Long getDayId();
    String getDay();
    String getSlot();
    Integer getCabinet();
    Boolean getIsActive();

}
