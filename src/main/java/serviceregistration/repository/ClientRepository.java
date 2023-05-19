package serviceregistration.repository;

import serviceregistration.model.Client;

public interface ClientRepository extends GenericRepository<Client> {

    Client findClientByLogin(String login);

    Client findClientByEmail(String email);

}
