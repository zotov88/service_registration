package serviceregistration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;

import java.util.List;

@Component
public class RegistrationMapper extends GenericMapper<Registration, RegistrationDTO> {


    public RegistrationMapper(ModelMapper modelMapper) {
        super(Registration.class, RegistrationDTO.class, modelMapper);
    }

    @Override
    protected void mapSpecificFields(Registration src, RegistrationDTO dst) {

    }

    @Override
    protected void mapSpecificFields(RegistrationDTO src, Registration dst) {

    }

    @Override
    protected void setupMapper() {

    }

    @Override
    protected List<Long> getIds(Registration entity) {
        return null;
    }
}
