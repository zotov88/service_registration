package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.GenericDTO;
import serviceregistration.model.GenericModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public abstract class GenericMapper<E extends GenericModel, D extends GenericDTO> implements Mapper<E, D> {

    private final Class<E> entityClass;
    private final Class<D> dtoClass;
    protected final ModelMapper modelMapper;

    public GenericMapper(Class<E> entityClass,
                         Class<D> dtoClass,
                         ModelMapper modelMapper) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.modelMapper = modelMapper;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, entityClass);
    }

    @Override
    public D toDTO(E entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
    }

    @Override
    public List<E> toEntities(List<D> dtos) {
        List<E> list = new ArrayList<>();
        for (D dto : dtos) {
            list.add(toEntity(dto));
        }
        return list;
    }

    @Override
    public List<D> toDTOs(List<E> entities) {
        List<D> list = new ArrayList<>();
        for (E entity : entities) {
            list.add(toDTO(entity));
        }
        return list;
    }

    protected Converter<E, D> toEntityConverter() {
        return c -> {
            E src = c.getSource();
            D dst = c.getDestination();
            mapSpecificFields(src, dst);
            return c.getDestination();
        };
    }

    protected Converter<D, E> toDTOConverter() {
        return c -> {
            D src = c.getSource();
            E dst = c.getDestination();
            mapSpecificFields(src, dst);
            return c.getDestination();
        };
    }

    protected abstract void mapSpecificFields(E src, D dst);

    protected abstract void mapSpecificFields(D src, E dst);

    @PostConstruct
    protected abstract void setupMapper();

    protected abstract List<Long> getIds(E entity);
}
