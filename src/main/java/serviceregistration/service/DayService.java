package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import serviceregistration.model.Day;
import serviceregistration.repository.DayRepository;

import java.util.List;

@Service
public class DayService {

    private final DayRepository dayRepository;

    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public List<Day> listAll() {
        return dayRepository.findAll();
    }

    public List<Day> getActualDays() {
        return dayRepository.findActualDays();
    }

    public Day getOne(Long dayId) {
        return dayRepository.findById(dayId).orElseThrow(() -> new NotFoundException("На этот день нет расписания"));
    }

    public Long getTodayId() {
        return dayRepository.findTodayId();
    }
}
