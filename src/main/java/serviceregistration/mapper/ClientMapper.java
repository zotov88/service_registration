package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.ClientDTO;
import serviceregistration.model.Client;
import serviceregistration.model.Registration;
import serviceregistration.repository.RegistrationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ClientMapper extends GenericMapper<Client, ClientDTO> {

    private final RegistrationRepository registrationRepository;

    public ClientMapper(ModelMapper modelMapper,
                        RegistrationRepository registrationRepository) {
        super(Client.class, ClientDTO.class, modelMapper);
        this.registrationRepository = registrationRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Client.class, ClientDTO.class)
                .addMappings(m -> m.skip(ClientDTO::setRegistrationIds)).setPostConverter(toEntityConverter());
        modelMapper.createTypeMap(ClientDTO.class, Client.class)
                .addMappings(m -> m.skip(Client::setRegistrations)).setPostConverter(toDTOConverter());
    }

    @Override
    protected void mapSpecificFields(ClientDTO src, Client dst) {
        if (!Objects.isNull(src.getRegistrationIds())) {
            dst.setRegistrations(registrationRepository.findAllById(src.getRegistrationIds()));
        } else {
            dst.setRegistrations(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Client src, ClientDTO dst) {
        dst.setRegistrationIds(getIds(src));
    }

    @Override
    protected List<Long> getIds(Client entity) {
        if (entity != null) {
            List<Long> list = new ArrayList<>();
            for (Registration registration : entity.getRegistrations()) {
                list.add(registration.getId());
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }
}
