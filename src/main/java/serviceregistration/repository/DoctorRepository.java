package serviceregistration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import serviceregistration.model.Doctor;

public interface DoctorRepository extends GenericRepository<Doctor> {

    Doctor findDoctorByLogin(String login);

    Doctor findDoctorByLoginAndIsDeletedFalse(String login);

    @Query(nativeQuery = true,
            value = """
                    select d.*
                    from doctors d
                        left join specializations s on d.specialization_id = s.id
                    where last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                        and s.title like '%' || coalesce(:specialization, '%')  || '%'
                    order by d.is_deleted, d.last_name, d.first_name, d.mid_name, s.title
                    """)
    Page<Doctor> findSearchDoctorsSort(@Param(value = "lastName") String lastName,
                                       @Param(value = "firstName") String firstName,
                                       @Param(value = "midName") String midName,
                                       @Param(value = "specialization") String specialization,
                                       Pageable page);

    @Query(nativeQuery = true,
            value = """
                    select d.*
                    from doctors d
                        left join specializations s on d.specialization_id = s.id
                    where last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                        and first_name ilike '%' || coalesce(:firstName, '%')  || '%'
                        and mid_name ilike '%' || coalesce(:midName, '%')  || '%'
                        and s.title like '%' || coalesce(:specialization, '%')  || '%'
                        and d.is_deleted = false
                    order by d.is_deleted, d.last_name, d.first_name, d.mid_name, s.title
                    """)
    Page<Doctor> findSearchDoctorsSortWithDeletedFalse(@Param(value = "lastName") String lastName,
                                                       @Param(value = "firstName") String firstName,
                                                       @Param(value = "midName") String midName,
                                                       @Param(value = "specialization") String specialization,
                                                       Pageable page);

    @Query(nativeQuery = true,
            value = """
                    select d.*
                    from doctors d
                        join specializations s on s.id = d.specialization_id
                    order by d.is_deleted, d.last_name, d.first_name, d.mid_name, s.title
                    """)
    Page<Doctor> findDoctorsSort(Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    select d.*
                    from doctors d
                        join specializations s on s.id = d.specialization_id
                    where is_deleted = false
                    order by d.is_deleted, d.last_name, d.first_name, d.mid_name, s.title
                    """)
    Page<Doctor> findAllDoctorsSortWithDeletedFalse(Pageable pageable);


    Doctor findDoctorByChangePasswordToken(String uuid);
}
