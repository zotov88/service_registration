package serviceregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serviceregistration.model.Day;

import java.util.List;

public interface DayRepository extends JpaRepository<Day, Long> {

    @Query(nativeQuery = true,
            value = """
                    select *
                    from days
                    where day >= TIMESTAMP 'today'
                    order by day
                    """)
    List<Day> findActualDays();
}
