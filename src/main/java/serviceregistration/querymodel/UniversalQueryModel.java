package serviceregistration.querymodel;

public interface UniversalQueryModel {

    Long getDoctorId();
    Long getDoctorSlotId();
    Long getRegistrationId();
    Long getDayId();
    String getDoctorFirstName();
    String getDoctorMidName();
    String getDoctorLastName();
    String getClientFirstName();
    String getClientMidName();
    String getClientLastName();
    String getDay();
    String getSlot();
    Integer getCabinet();
    String getSpecialization();
    Boolean getIsDoctorDeleted();
    Boolean getIsActive();
    Boolean getIsDeleted();
    Boolean getRegistered();
}
