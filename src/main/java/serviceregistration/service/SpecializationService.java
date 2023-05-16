package serviceregistration.service;

import org.springframework.stereotype.Service;
import serviceregistration.model.Specialization;
import serviceregistration.repository.SpecializationRepository;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> listAll() {
        return specializationRepository.findAll();
    }
}
