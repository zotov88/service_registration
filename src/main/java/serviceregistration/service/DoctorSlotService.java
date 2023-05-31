package serviceregistration.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.dto.querymodel.DoctorDay;
import serviceregistration.dto.querymodel.DoctorSchedule;
import serviceregistration.dto.querymodel.DoctorSlotForSchedule;
import serviceregistration.dto.querymodel.SlotRegistered;
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

    public Page<DoctorDay> groupByDoctorSlot(Pageable pageable) {
        Page<DoctorDay> doctorDayPaginated = doctorSlotRepository.groupByDoctorSlot(pageable);
        List<DoctorDay> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }

    public Page<DoctorDay> findAmongGroupByDoctorSlot(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<DoctorDay> doctorDayPaginated = doctorSlotRepository.searchGroupByDoctorSlot(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                pageable);
        List<DoctorDay> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }

    public Page<DoctorSlotForSchedule> getActualSchedule(Pageable pageable) {
        Page<DoctorSlotForSchedule> doctorSlotsPaginated = doctorSlotRepository.findActualScheduleGroup(pageable);
        List<DoctorSlotForSchedule> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<DoctorSlotForSchedule> getArchiveSchedule(PageRequest pageable) {
        Page<DoctorSlotForSchedule> doctorSlotsPaginated = doctorSlotRepository.findArchiveScheduleGroup(pageable);
        List<DoctorSlotForSchedule> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<DoctorSlotForSchedule> findAmongActualSchedule(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<DoctorSlotForSchedule> doctorSlotPage = doctorSlotRepository.searchActualScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageable);
        List<DoctorSlotForSchedule> result = doctorSlotPage.getContent();
        return new PageImpl<>(result, pageable, doctorSlotPage.getTotalElements());
    }

    public Page<DoctorSlotForSchedule> findAmongAllSchedule(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<DoctorSlotForSchedule> doctorSlotPage = doctorSlotRepository.searchArchiveScheduleGroup(
                doctorSlotSearchDTO.getLastName(),
                doctorSlotSearchDTO.getFirstName(),
                doctorSlotSearchDTO.getMidName(),
                doctorSlotSearchDTO.getSpecialization(),
                doctorSlotSearchDTO.getDay(),
                doctorSlotSearchDTO.getCabinetNumber(),
                pageable);
        List<DoctorSlotForSchedule> result = doctorSlotPage.getContent();
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

    public Page<DoctorSchedule> getScheduleByDoctor(Pageable pageable, Long doctorId) {
        Page<DoctorSchedule> doctorSchedulePage = doctorSlotRepository.findScheduleByDoctorId(pageable, doctorId);
        List<DoctorSchedule> result = doctorSchedulePage.getContent();
        return new PageImpl<>(result, pageable, doctorSchedulePage.getTotalElements());
    }

    public List<SlotRegistered> getScheduleByDoctorToday(Long doctorId) {
        return doctorSlotRepository.findScheduleByDoctorIdToday(doctorId);
    }

    public List<SlotRegistered> getSlotsForDoctorDay(Long doctorId, Long dayId) {
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
}
