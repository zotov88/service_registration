package serviceregistration.dto.querymodel;

public interface ClientRegistration extends DoctorName {

    Long getRegistrationId();
    String getSpecialization();
    String getDay();
    String getSlot();
    Integer getCabinet();
    Boolean getIsActive();
}
