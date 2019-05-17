package repository;

import enums.PaymentType;
import model.Client;
import model.VipClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ClientsRepositoryDB implements IClientsRepository{
    private String url = "jdbc:mysql://localhost/cineJohn";
    private String user = "root";
    private String password = "";

    @Override
    public Client findByName(String name) {
                List<Client> clients = new ArrayList<>();
        Client client = null;
        String sql = "SELECT * FROM clients WHERE name=?";
        try(Connection con = DriverManager.getConnection(url, user,password);
            PreparedStatement statement = con.prepareStatement(sql);
        ){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("idclients");
                String namee = resultSet.getString("name");
                List<String> seatss = Arrays.asList(resultSet.getString("seats").split("\\| "));
                List<Integer> seats = seatss.stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                PaymentType paymentType = PaymentType.valueOf(resultSet.getString("payment_type"));
                String spectaclee = resultSet.getString("spectacle");
                String  is_vip = resultSet.getString("is_vip");
                if (is_vip.equals("true")){
                    client = new VipClient(name, new ArrayList<>(seats));
                }
                else{
                    client = new Client(name, seats);
                    client.setPaymentType(paymentType);
                }

                client.setSpectacle(spectaclee);
                client.setId(id);

                clients.add(client);
                break;
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return client;
    }

    @Override
    public Client findByIndex(int index) {
        Client client = null;

        String sql = "SELECT * FROM clients WHERE idclients=?";
        try(Connection con = DriverManager.getConnection(url, user,password);
            PreparedStatement statement = con.prepareStatement(sql);
        ){
            statement.setInt(1, index);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("idclients");
                String name = resultSet.getString("name");
                List<String> seatss = Arrays.asList(resultSet.getString("seats").split("\\| "));
                List<Integer> seats = seatss.stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                PaymentType paymentType = PaymentType.valueOf(resultSet.getString("payment_type"));
                String spectaclee = resultSet.getString("spectacle");
                String  is_vip = resultSet.getString("is_vip");
                if (is_vip.equals("true")){
                    client = new VipClient(name, new ArrayList<>(seats));
                }
                else{
                    client = new Client(name, seats);
                    client.setPaymentType(paymentType);
                }

                client.setSpectacle(spectaclee);
                client.setId(id);
                break;
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return client;
    }

    @Override
    public List<Client> findBySpectacle(String spectacle) {
        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients WHERE spectacle=?";
        try(Connection con = DriverManager.getConnection(url, user,password);
            PreparedStatement statement = con.prepareStatement(sql);
        ){
            statement.setString(1, spectacle);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("idclients");
                String name = resultSet.getString("name");
                List<String> seatss = Arrays.asList(resultSet.getString("seats").split("\\| "));
                List<Integer> seats = seatss.stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                PaymentType paymentType = PaymentType.valueOf(resultSet.getString("payment_type"));
                String spectaclee = resultSet.getString("spectacle");
                String  is_vip = resultSet.getString("is_vip");
                Client client;
                if (is_vip.equals("true")){
                    client = new VipClient(name, new ArrayList<>(seats));
                }
                else{
                    client = new Client(name, seats);
                    client.setPaymentType(paymentType);
                }

                client.setSpectacle(spectaclee);
                client.setId(id);

                clients.add(client);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return clients;
    }

    @Override
    public List<Client> findAll()
    {
        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients";

        try (Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();
        ){
            while (resultSet.next()){
                int id = resultSet.getInt("idclients");
                String name = resultSet.getString("name");
                List<String> seatss = Arrays.asList(resultSet.getString("seats").split("\\| "));
                List<Integer> seats = seatss.stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                PaymentType paymentType = PaymentType.valueOf(resultSet.getString("payment_type"));
                String spectacle = resultSet.getString("spectacle");
                String  is_vip = resultSet.getString("is_vip");
                Client client;
                if (is_vip.equals("true")){
                    client = new VipClient(name, new ArrayList<>(seats));
                }
                else{
                    client = new Client(name, seats);
                    client.setPaymentType(paymentType);
                }

                client.setSpectacle(spectacle);
                client.setId(id);

                clients.add(client);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO clients VALUES(NULL, ?, ?, ?, ?, ?)";
        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = con.prepareStatement(sql)
        ){
            preparedStatement.setString(1,client.getName());
            StringBuilder sb = new StringBuilder();
            client.getSeats().forEach(e -> {
                sb.append(e + "| ");
            });
            sb.replace(sb.length()-2, sb.length(),"");
            preparedStatement.setString(2, sb.toString());
            preparedStatement.setString(3,client.getPaymentType().toString());
            preparedStatement.setString(4,client.getSpectacle());
            if(client instanceof VipClient){
                preparedStatement.setString(5, "true");
            }else{
                preparedStatement.setString(5, "false");
            }

            preparedStatement.execute();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteClient(Client client) {
        String sql = "DELETE FROM clients where name = ?";
        ISeatRepository repository = new SeatRepository();
        ISpectaclesRepository repositorySpec = new SpectaclesRepositoryDB();
        client.getSeats().forEach(e -> {
            repository.unsetOccupied(e, Integer.parseInt(repositorySpec.findByName(client.getSpectacle()).getId()));
        });

        try(Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, client.getName());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
