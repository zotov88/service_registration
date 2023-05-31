package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.mapper.RegistrationMapper;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;
import serviceregistration.repository.RegistrationRepository;

import java.util.List;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final DoctorSlotRepository doctorSlotRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository,
                             DoctorSlotMapper mapper,
                             RegistrationRepository registrationRepository,
                             RegistrationMapper registrationMapper) {
        super(doctorSlotRepository, mapper);
        this.doctorSlotRepository = doctorSlotRepository;
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
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

    public Page<UniversalQueryModel> groupByDoctorSlot(Pageable pageable) {
        Page<UniversalQueryModel> doctorDayPaginated = doctorSlotRepository.groupByDoctorSlot(pageable);
        List<UniversalQueryModel> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> findAmongGroupByDoctorSlot(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<UniversalQueryModel> doctorDayPaginated = doctorSlotRepository.searchGroupByDoctorSlot(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                pageable);
        List<UniversalQueryModel> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> getActualSchedule(Pageable pageable) {
        Page<UniversalQueryModel> doctorSlotsPaginated = doctorSlotRepository.findActualScheduleGroup(pageable);
        List<UniversalQueryModel> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> getArchiveSchedule(PageRequest pageable) {
        Page<UniversalQueryModel> doctorSlotsPaginated = doctorSlotRepository.findArchiveScheduleGroup(pageable);
        List<UniversalQueryModel> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> findAmongActualSchedule(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<UniversalQueryModel> doctorSlotPage = doctorSlotRepository.searchActualScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageable);
        List<UniversalQueryModel> result = doctorSlotPage.getContent();
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public Page<UniversalQueryModel> findAmongAllSchedule(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<UniversalQueryModel> doctorSlotPage = doctorSlotRepository.searchArchiveScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageable);
        List<UniversalQueryModel> result = doctorSlotPage.getContent();
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public Page<DoctorSlotDTO> getActualDoctorSlot(Pageable pageable) {
        Page<DoctorSlot> doctorSlotsPaginated = doctorSlotRepository.findActualSchedule(pageable);
        List<DoctorSlotDTO> result = mapper.toDTOs(doctorSlotsPaginated.getContent());
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public List<Long> getTimeForDay(Long doctorId, Long dayId) {
        return doctorSlotRepository.findAllTimeForDoctorDay(doctorId, dayId);
    }

    public Page<UniversalQueryModel> getScheduleByDoctor(Pageable pageable, Long doctorId) {
        Page<UniversalQueryModel> doctorSchedulePage = doctorSlotRepository.findScheduleByDoctorId(pageable, doctorId);
        List<UniversalQueryModel> result = doctorSchedulePage.getContent();
        return new PageImpl<>(result, pageable, doctorSchedulePage.getTotalElements());
    }

    public List<UniversalQueryModel> getScheduleByDoctorToday(Long doctorId) {
        return doctorSlotRepository.findScheduleByDoctorIdToday(doctorId);
    }

    public List<UniversalQueryModel> getSlotsForDoctorDay(Long doctorId, Long dayId) {
        return doctorSlotRepository.getSlotsOneDayForDoctor(doctorId, dayId);
    }

    public void restore(Long doctorId, Long dayId) {
        doctorSlotRepository.unMarkAsDeletedSlots(doctorId, dayId);
    }


    public List<DoctorSlotDTO> getAllByDoctorId(Long doctorId) {
        return mapper.toDTOs(doctorSlotRepository.findAllByDoctorId(doctorId));
    }

    public List<Long> getAllRegistrationsByDoctorId(Long doctorId) {
        return doctorSlotRepository.findAllRegistrationsByDoctorId(doctorId);
    }

    public Integer getCabinetByDoctorIdAndDayId(Long doctorId, Long dayId) {
        return doctorSlotRepository.findCabinetByDoctorIdAndDayId(doctorId, dayId);
    }

    public List<DoctorSlotDTO> listAllActiveDoctorSlotsByClientId(Long clientId) {
        return mapper.toDTOs(doctorSlotRepository.findAllActiveDoctorSlotsByClientId(clientId));
    }
}
