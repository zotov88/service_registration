package serviceregistration.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import serviceregistration.dto.ClientDTO;
import serviceregistration.dto.ClientSearchDTO;
import serviceregistration.mapper.ClientMapper;
import serviceregistration.model.Client;
import serviceregistration.repository.ClientRepository;
import serviceregistration.service.dataconstants.ClientTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientServiceTest extends GenericTest<Client, ClientDTO> {

    public ClientServiceTest() {
        super();
        repository = Mockito.mock(ClientRepository.class);
        mapper = Mockito.mock(ClientMapper.class);
        service = new ClientService(
                (ClientRepository) repository,
                (ClientMapper) mapper,
                Mockito.mock(UserService.class),
                Mockito.mock(BCryptPasswordEncoder.class),
                Mockito.mock(RegistrationService.class),
                Mockito.mock(DoctorSlotService.class),
                Mockito.mock(JavaMailSender.class)
        );
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(ClientTestData.CLIENT_LIST);
        Mockito.when(mapper.toDTOs(ClientTestData.CLIENT_LIST)).thenReturn(ClientTestData.CLIENT_DTO_LIST);
        List<ClientDTO> clientDTOS = service.listAll();
        assertEquals(ClientTestData.CLIENT_LIST.size(), repository.findAll().size());
        assertEquals(ClientTestData.CLIENT_DTO_LIST.size(), clientDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(ClientTestData.CLIENT_1));
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_1)).thenReturn(ClientTestData.CLIENT_DTO_1);
        ClientDTO clientDTO = service.getOne(1L);
        assertEquals(clientDTO, ClientTestData.CLIENT_DTO_1);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(repository.save(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        ClientDTO clientDTO = service.create(ClientTestData.CLIENT_DTO_2);
        assertEquals(clientDTO, ClientTestData.CLIENT_DTO_2);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(repository.save(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        ClientDTO clientDTO = service.create(ClientTestData.CLIENT_DTO_2);
        assertEquals(clientDTO, ClientTestData.CLIENT_DTO_2);
    }

    @Test
    @Order(5)
    @Override
    protected void delete() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(ClientTestData.CLIENT_1));
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_1)).thenReturn(ClientTestData.CLIENT_DTO_1);
        Mockito.when(service.getOne(1L)).thenReturn(ClientTestData.CLIENT_DTO_1);
        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_1)).thenReturn(ClientTestData.CLIENT_1);
        Mockito.when(repository.save(ClientTestData.CLIENT_1)).thenReturn(ClientTestData.CLIENT_1);
        ((ClientService) service).softDelete(1L);
        assertTrue(ClientTestData.CLIENT_DTO_1.isDeleted());
    }

    @Test
    @Order(6)
    @Override
    protected void restore() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(ClientTestData.CLIENT_1));
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_1)).thenReturn(ClientTestData.CLIENT_DTO_1);
        Mockito.when(mapper.toEntity(ClientTestData.CLIENT_DTO_1)).thenReturn(ClientTestData.CLIENT_1);
        Mockito.when(repository.save(ClientTestData.CLIENT_1)).thenReturn(ClientTestData.CLIENT_1);
        ((ClientService) service).restore(1L);
        assertFalse(ClientTestData.CLIENT_DTO_1.isDeleted());
    }

    @Test
    @Order(7)
    public void searchClients() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        ClientSearchDTO clientSearchDTO = new ClientSearchDTO(
                ClientTestData.CLIENT_1.getLastName(),
                ClientTestData.CLIENT_1.getFirstName(),
                ClientTestData.CLIENT_1.getMidName()
        );
        Mockito.when(((ClientRepository) repository).searchClients(
                clientSearchDTO.getLastName(),
                clientSearchDTO.getFirstName(),
                clientSearchDTO.getMidName(),
                pageRequest
        )).thenReturn(new PageImpl<>(ClientTestData.CLIENT_LIST));
        Mockito.when(mapper.toDTOs(ClientTestData.CLIENT_LIST)).thenReturn(ClientTestData.CLIENT_DTO_LIST);
        Page<ClientDTO> clientDTOPage = ((ClientService) service).findClients(clientSearchDTO, pageRequest);
        assertEquals(ClientTestData.CLIENT_DTO_LIST, clientDTOPage.getContent());
    }

    @Test
    @Order(8)
    public void searchClientsWithIsDeletedFalse() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        ClientTestData.CLIENT_3.setDeleted(true);
        List<Client> clients = new ArrayList<>();
        for (Client client : ClientTestData.CLIENT_LIST) {
            if (!client.isDeleted()) {
                clients.add(client);
            }
        }
        Mockito.when(((ClientRepository) repository).searchClientsWithDeletedFalse(
                "1", "2", "3",
                pageRequest)).thenReturn(new PageImpl<>(clients));
        Mockito.when(mapper.toDTOs(clients)).thenReturn(
                ClientTestData.CLIENT_DTO_LIST.stream().filter(Predicate.not(ClientDTO::isDeleted)).toList());
        Page<ClientDTO> clientDTOS = ((ClientService) service).findClientsWithDeletedFalse(
                new ClientSearchDTO("1", "2", "3"), pageRequest);
        assertEquals(ClientTestData.CLIENT_DTO_LIST, clientDTOS.getContent());

    }

    @Test
    @Order(9)
    public void allClientsWithIsDeletedFalse() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        ClientTestData.CLIENT_3.setDeleted(true);
        List<Client> doctors = new ArrayList<>();
        for (Client doctor : ClientTestData.CLIENT_LIST) {
            if (!doctor.isDeleted()) {
                doctors.add(doctor);
            }
        }
        Mockito.when(((ClientRepository)repository).findListAllClientsWithDeletedFalse(pageRequest))
                .thenReturn(new PageImpl<>(doctors));
        Mockito.when(mapper.toDTOs(doctors)).thenReturn(
                ClientTestData.CLIENT_DTO_LIST.stream().filter(Predicate.not(ClientDTO::isDeleted)).toList());
        Page<ClientDTO> doctorDTOS = ((ClientService) service).listAllClientsWithDeletedFalse(pageRequest);
        assertEquals(ClientTestData.CLIENT_DTO_LIST, doctorDTOS.getContent());
    }

    @Test
    @Order(10)
    public void findByLogin() {
        ClientTestData.CLIENT_1.setDeleted(true);
        Mockito.when(((ClientRepository) repository).findClientByLoginAndIsDeletedFalse(
                ClientTestData.CLIENT_1.getLogin())).thenReturn(null);
        assertNull(((ClientService) service).getClientByLogin(ClientTestData.CLIENT_1.getLogin()));

        Mockito.when(((ClientRepository) repository).findClientByLoginAndIsDeletedFalse(
                ClientTestData.CLIENT_2.getLogin())).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        ClientDTO clientDTO = ((ClientService) service).getClientByLogin(ClientTestData.CLIENT_2.getLogin());
        assertEquals(clientDTO, ClientTestData.CLIENT_DTO_2);
    }

    @Test
    @Order(11)
    public void findByEmail() {
        ClientTestData.CLIENT_1.setDeleted(true);
        Mockito.when(((ClientRepository) repository).findClientByEmailAndIsDeletedFalse(
                ClientTestData.CLIENT_1.getLogin())).thenReturn(null);
        assertNull(((ClientService) service).getClientByEmail(ClientTestData.CLIENT_1.getEmail()));

        Mockito.when(((ClientRepository) repository).findClientByEmailAndIsDeletedFalse(
                ClientTestData.CLIENT_2.getEmail())).thenReturn(ClientTestData.CLIENT_2);
        Mockito.when(mapper.toDTO(ClientTestData.CLIENT_2)).thenReturn(ClientTestData.CLIENT_DTO_2);
        ClientDTO clientDTO = ((ClientService) service).getClientByEmail(ClientTestData.CLIENT_2.getEmail());
        assertEquals(clientDTO, ClientTestData.CLIENT_DTO_2);
    }
}
