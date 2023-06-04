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

    @Query(nativeQuery = true,
            value = """
                    select id
                    from days
                    where days.day = date(now())
                    """)
    Long findTodayId();

    @Query(nativeQuery = true,
            value = """
                    select *
                    from days
                    where day >= TIMESTAMP 'today'
                    order by day
                    limit :count
                    """)
    List<Day> findFirstActualDays(int count);

    @Query(nativeQuery = true,
            value = """
                    select *
                    from days
                    where cast(day as text) = :date
                    """)
    Day findDayByDate(String date);
}
