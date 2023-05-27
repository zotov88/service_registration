package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.custommodel.DoctorDay;
import serviceregistration.dto.custommodel.DoctorSlotIdTimeSlot;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             GenericMapper<DoctorSlot, DoctorSlotDTO> mapper) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
    }

    public void addSchedule(final Long doctorId, final Long dayId, final Long cabinetId) {
         doctorSlotRepository.addSchedule(doctorId, dayId, cabinetId);
    }

    public void deleteSchedule(final Long doctorId, final Long dayId) {
        doctorSlotRepository.deleteAllByDoctorIdAndDayId(doctorId, dayId);
    }

    public DoctorSlotDTO getDoctorSlotByCabinetAndDay(final Long cabinetId, final Long dayId) {
        return mapper.toDTO(doctorSlotRepository.findFirstByCabinetIdAndDayId(cabinetId, dayId));
    }

    public DoctorSlotDTO getDoctorSlotByDoctorAndDay(final Long doctorId, final Long dayId) {
        return mapper.toDTO(doctorSlotRepository.findFirstByDoctorIdAndDayId(doctorId, dayId));
    }

    public List<DoctorDay> groupByDoctorSlot() {
        return doctorSlotRepository.groupByDoctorSlot();
    }

    public Page<DoctorDay> groupByDoctorSlot(Pageable pageable) {
        Page<DoctorDay> doctorDayPaginated = doctorSlotRepository.groupByDoctorSlot(pageable);
        List<DoctorDay> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }



    public Page<DoctorSlotDTO> getAllDoctorSlot(Pageable pageable) {
        Page<DoctorSlot> doctorSlotsPaginated = doctorSlotRepository.findAllNotLessThanToday(pageable);
        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotsPaginated.getContent());
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public List<Long> getTimeForDay(Long doctorId, Long dayId) {
        return doctorSlotRepository.findAllTimeForDoctorDay(doctorId, dayId);
    }

    public List<DoctorSlotIdTimeSlot> getDoctorSlotsIdsAndTimeSlotsFree(Long doctorId, Long dayId) {
        return doctorSlotRepository.findAllDoctorslotIdsAndTimeSlotsFree(doctorId, dayId);
    }

//    public List<DoctorSlotDTO> listAllIdRegistrationsByClientId(Long clientId) {
//        return mapper.toDTOs(doctorSlotRepository.findAllByClientReserved(clientId));
//    }
}
