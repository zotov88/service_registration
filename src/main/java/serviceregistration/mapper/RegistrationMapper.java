package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Component
public class RegistrationMapper extends GenericMapper<Registration, RegistrationDTO> {

    private final ClientRepository clientRepository;
    private final DoctorSlotRepository doctorSlotRepository;

    public RegistrationMapper(ModelMapper modelMapper,
                              ClientRepository clientRepository,
                              DoctorSlotRepository doctorSlotRepository) {
        super(Registration.class, RegistrationDTO.class, modelMapper);
        this.clientRepository = clientRepository;
        this.doctorSlotRepository = doctorSlotRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Registration.class, RegistrationDTO.class)
                .addMappings(m -> m.skip(RegistrationDTO::setClientId))
                .addMappings(m -> m.skip(RegistrationDTO::setDoctorSlotId)).setPostConverter(toEntityConverter());

        modelMapper.createTypeMap(RegistrationDTO.class, Registration.class)
                .addMappings(m -> m.skip(Registration::setClient))
                .addMappings(m -> m.skip(Registration::setDoctorSlot)).setPostConverter(toDTOConverter());
    }

//    @PostConstruct
//    public void setupMapper() {
//        super.modelMapper.createTypeMap(BookRentInfo.class, BookRentInfoDTO.class)
//                .addMappings(m -> m.skip(BookRentInfoDTO::setUserId))
//                .addMappings(m -> m.skip(BookRentInfoDTO::setBookId))
//                .addMappings(m -> m.skip(BookRentInfoDTO::setBookDTO))
//                .setPostConverter(toDTOConverter());
//
//        super.modelMapper.createTypeMap(BookRentInfoDTO.class, BookRentInfo.class)
//                .addMappings(m -> m.skip(BookRentInfo::setUser))
//                .addMappings(m -> m.skip(BookRentInfo::setBook))
//                .setPostConverter(toEntityConverter());
//    }

    @Override
    protected void mapSpecificFields(RegistrationDTO src, Registration dst) {
        dst.setClient(clientRepository.findById(src.getClientId()).orElseThrow(() -> new NotFoundException("Такого клиента нет")));
        dst.setDoctorSlot(doctorSlotRepository.findById(src.getDoctorSlotId()).orElseThrow(() -> new NotFoundException("Нет такого временного слота")));
    }

    @Override
    protected void mapSpecificFields(Registration src, RegistrationDTO dst) {
        dst.setClientId(src.getClient().getId());
        dst.setDoctorSlotId(src.getDoctorSlot().getId());
    }

    @Override
    protected List<Long> getIds(Registration entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}
