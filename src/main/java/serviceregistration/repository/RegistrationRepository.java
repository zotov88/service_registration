package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.dto.custommodel.ClientRegistration;
import serviceregistration.model.Registration;

import java.util.List;

public interface RegistrationRepository
        extends GenericRepository<Registration> {

    Page<Registration> getAllByClientId(Pageable pageable, Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select id
                    from registrations
                    where client_id = :clientId""")
    List<Long> getAllByClientId(Long clientId);

    @Query(nativeQuery = true,
            value = """
                    select d.first_name as DoctorFirstName, d.mid_name as DoctorMidName, d.last_name as DoctorLastName,
                     sp.title as Specialization, d2.day as Day, s.time_slot as Slot, c2.number as Cabinet
                    from doctors_slots ds
                    join registrations r on ds.id = r.doctor_slot_id
                    join doctors d on d.id = ds.doctor_id
                    join specializations sp on sp.id = d.specialization_id
                    join days d2 on ds.day_id = d2.id
                    join cabinets c2 on c2.id = ds.cabinet_id
                    join slots s on s.id = ds.slot_id
                    where r.client_id = :clientId
                    """)
    List<ClientRegistration> getAllRegistrationsByClient(Long clientId);
}
