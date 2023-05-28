package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.model.Doctor;

public interface DoctorRepository extends GenericRepository<Doctor> {

    Doctor findDoctorByLogin(String login);

    @Query(nativeQuery = true,
            value = """
                    select d.*
                    from doctors d
                        left join specializations s on d.specialization_id = s.id
                    where last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                        and s.title like '%' || coalesce(:specialization, '%')  || '%'
                    """)
    Page<Doctor> searchDoctors(@Param(value = "lastName") String lastName,
                               @Param(value = "firstName") String firstName,
                               @Param(value = "midName") String midName,
                               @Param(value = "specialization") String specialization,
                               Pageable page);

}
