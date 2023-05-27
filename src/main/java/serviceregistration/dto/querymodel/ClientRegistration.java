package serviceregistration.dto.querymodel;

public interface ClientRegistration extends DoctorName {

    String getSpecialization();

    String getDay();

    String getSlot();

    Integer getCabinet();
}
