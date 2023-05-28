package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.model.Slot;
import serviceregistration.repository.SlotRepository;

import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<Slot> listAll() {
        return slotRepository.findAll();
    }

}
