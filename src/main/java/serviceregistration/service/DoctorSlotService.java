package serviceregistration.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serviceregistration.constants.MailConstants;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.dto.DoctorSlotSearchDTO;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.mapper.DoctorSlotMapper;
import serviceregistration.model.Day;
import serviceregistration.model.DoctorSlot;
import serviceregistration.querymodel.UniversalQueryModel;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Transactional
@Service
public class DoctorSlotService extends GenericService<DoctorSlot, DoctorSlotDTO> {

    private final RegistrationService registrationService;
    private final MailSenderService mailSenderService;

    public DoctorSlotService(DoctorSlotRepository repository,
                             DoctorSlotMapper mapper,
                             @Lazy RegistrationService registrationService,
                             @Lazy MailSenderService mailSenderService) {
        super(repository, mapper);
        this.registrationService = registrationService;
        this.mailSenderService = mailSenderService;
    }

    public void addSchedule(final Long doctorId, final Long dayId, final Long cabinetId) {
        ((DoctorSlotRepository) repository).addSchedule(doctorId, dayId, cabinetId);
    }

    public DoctorSlotDTO getDoctorSlotByCabinetAndDay(final Long cabinetId, final Long dayId) {
        return mapper.toDTO(((DoctorSlotRepository) repository).findFirstByCabinetIdAndDayId(cabinetId, dayId));
    }

    public DoctorSlotDTO getDoctorSlotByDoctorAndDay(final Long doctorId, final Long dayId) {
        return mapper.toDTO(((DoctorSlotRepository) repository).findFirstByDoctorIdAndDayId(doctorId, dayId));
    }

    public Page<UniversalQueryModel> groupByDoctorSlot(Pageable pageable) {
        Page<UniversalQueryModel> doctorDayPaginated = ((DoctorSlotRepository) repository).findGroupByDoctorSlot(pageable);
        List<UniversalQueryModel> result = doctorDayPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorDayPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> findAmongGroupByDoctorSlot(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<UniversalQueryModel> doctorDayPaginated = ((DoctorSlotRepository) repository).searchGroupByDoctorSlot(
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
        Page<UniversalQueryModel> doctorSlotsPaginated = ((DoctorSlotRepository) repository).findActualScheduleGroup(pageable);
        List<UniversalQueryModel> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> getArchiveSchedule(PageRequest pageable) {
        Page<UniversalQueryModel> doctorSlotsPaginated = ((DoctorSlotRepository) repository).findArchiveScheduleGroup(pageable);
        List<UniversalQueryModel> result = doctorSlotsPaginated.getContent();
        return new PageImpl<>(result, pageable, doctorSlotsPaginated.getTotalElements());
    }

    public Page<UniversalQueryModel> findAmongActualSchedule(Pageable pageable, DoctorSlotSearchDTO doctorSlotSearchDTO) {
        Page<UniversalQueryModel> doctorSlotPage = ((DoctorSlotRepository) repository).searchActualScheduleGroup(
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
        Page<UniversalQueryModel> doctorSlotPage = ((DoctorSlotRepository) repository).searchArchiveScheduleGroup(
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

    public Page<UniversalQueryModel> getActualScheduleByDoctor(Pageable pageable, Long doctorId) {
        Page<UniversalQueryModel> doctorSchedulePage = ((DoctorSlotRepository) repository).findActualScheduleByDoctorId(pageable, doctorId);
        List<UniversalQueryModel> result = doctorSchedulePage.getContent();
        return new PageImpl<>(result, pageable, doctorSchedulePage.getTotalElements());
    }

    public Page<UniversalQueryModel> getArchiveScheduleByDoctor(Pageable pageable, Long doctorId) {
        Page<UniversalQueryModel> doctorSchedulePage = ((DoctorSlotRepository) repository).findArchiveScheduleByDoctor(pageable, doctorId);
        List<UniversalQueryModel> result = doctorSchedulePage.getContent();
        return new PageImpl<>(result, pageable, doctorSchedulePage.getTotalElements());
    }

    public Page<UniversalQueryModel> searchArchiveScheduleByDoctor(Pageable pageable,
                                                                   final Long doctorId,
                                                                   final Day day) {
        String dayVal = day.getDay() == null ? "" : day.getDay().toString();
        Page<UniversalQueryModel> doctorSchedulePage = ((DoctorSlotRepository) repository).findSearchArchiveScheduleByDoctor(pageable, doctorId, dayVal);
        List<UniversalQueryModel> result = doctorSchedulePage.getContent();
        return new PageImpl<>(result, pageable, doctorSchedulePage.getTotalElements());
    }

    public List<UniversalQueryModel> getSlotsOneDayForDoctor(Long doctorId, Long dayId) {
        return ((DoctorSlotRepository) repository).findSlotsOneDayForDoctor(doctorId, dayId);
    }

    public List<UniversalQueryModel> getSlotsOneDayForClient(Long doctorId, Long dayId) {
        return ((DoctorSlotRepository) repository).findSlotsOneDayForClient(doctorId, dayId);
    }

    public void restore(Long doctorId, Long dayId) {
        ((DoctorSlotRepository) repository).unMarkAsDeletedSlots(doctorId, dayId);
    }


    public List<DoctorSlotDTO> getAllByDoctorId(Long doctorId) {
        return mapper.toDTOs(((DoctorSlotRepository) repository).findAllByDoctorId(doctorId));
    }

    public Integer getCabinetByDoctorIdAndDayId(Long doctorId, Long dayId) {
        return ((DoctorSlotRepository) repository).findCabinetByDoctorIdAndDayId(doctorId, dayId);
    }

    public List<DoctorSlotDTO> listAllActiveDoctorSlotsByClientId(Long clientId) {
        return mapper.toDTOs(((DoctorSlotRepository) repository).findAllActiveDoctorSlotsByClientId(clientId));
    }

    public List<Long> getPosiblyCancelMeet(Long doctorId, Long dayId) {
        return ((DoctorSlotRepository) repository).findPosiblyCancelMeet(doctorId, dayId);
    }

    public void deleteSoft(final Long doctorId, final Long dayId) {
        List<DoctorSlotDTO> doctorSlotDTOS = mapper.toDTOs(((DoctorSlotRepository) repository).findDSWhereRegistrationsIsActive(doctorId, dayId));
        for (DoctorSlotDTO doctorSlotDTO : doctorSlotDTOS) {
            RegistrationDTO registrationDTO = registrationService.getOnByDoctorSlotId(doctorSlotDTO.getId());
            registrationDTO.setIsActive(false);
            doctorSlotDTO.setIsRegistered(false);
            mailSenderService.sendMessage(
                    registrationDTO.getId(),
                    MailConstants.MAIL_SUBJECT_FOR_REGISTRATION_CANCEL,
                    MailConstants.MAIL_BODY_FOR_REGISTRATION_CANCEL
            );
            update(doctorSlotDTO);
            registrationService.update(registrationDTO);
        }
        ((DoctorSlotRepository) repository).markAsDeletedSlots(doctorId, dayId);
    }

}