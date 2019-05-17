package repository;

import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientsRepository implements IClientsRepository{

    private List<Client> clients = new ArrayList<>();

    @Override
    public Client findByName(String name) {
        return clients.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public Client findByIndex(int index) {
        return clients.get(index);
    }

    @Override
    public List<Client> findBySpectacle(String spectacle) {
        return clients.stream()
                .filter(e -> e.getSpectacle().equals(spectacle))
                .collect(Collectors.toList());
    }

    @Override
    public List<Client> findAll() {
        return clients;
    }

    @Override
    public void addClient(Client client) {
        clients.add(client);
    }

    @Override
    public void deleteClient(Client client) {
        clients.remove(client);
    }
}
