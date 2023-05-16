package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.model.Cabinet;
import serviceregistration.repository.CabinetRepository;

import java.util.List;

@Service
public class CabinetService {

    private final CabinetRepository cabinetRepository;

    public CabinetService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    public List<Cabinet> listAll() {
        return cabinetRepository.findAll();
    }
}
