package repository;

import model.Client;

import java.util.List;

public interface IClientsRepository {

    Client findByName(String name);
    Client findByIndex(int index);
    List<Client> findBySpectacle(String spectacle);
    List<Client> findAll();
    void addClient(Client client);
    void deleteClient(Client client);
}
