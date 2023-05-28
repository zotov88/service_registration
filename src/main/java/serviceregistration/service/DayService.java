package serviceregistration.service;

import org.springframework.stereotype.Service;
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
}
