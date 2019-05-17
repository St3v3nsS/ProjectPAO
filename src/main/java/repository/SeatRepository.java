package repository;

import model.Seat;
import model.VipSeat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatRepository implements ISeatRepository {
    private String url = "jdbc:mysql://localhost/cineJohn";
    private String user = "root";
    private String password = "";

    @Override
    public void updateOccupied(Integer seat, int idSpec) {
        String sql = "UPDATE seats SET occupied = ? WHERE id_spec = ? AND number = ?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, "true");
            statement.setInt(2, idSpec);
            statement.setInt(3, seat);

            statement.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<Seat> getSeatsForSpectacle(int idSpec) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE id_spec = ?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, idSpec);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int number =resultSet.getInt("number");
                boolean occupied = resultSet.getBoolean("occupied");
                int row = resultSet.getInt("row");
                double price = resultSet.getDouble("price");
                String type = resultSet.getString("type");
                Seat seat;
                if (type.equals("Armchair")){
                    seat = new VipSeat(number, occupied, row, price, 0);
                    seat.setType(type);
                }else {
                    seat = new Seat(number, occupied, row, price);
                    seat.setType(type);
                }

                seats.add(seat);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return seats;
    }

    @Override
    public void unsetOccupied(Integer seat, int idSpec) {
        String sql = "UPDATE seats SET occupied = ? WHERE id_spec = ? AND number = ?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, "false");
            statement.setInt(2, idSpec);
            statement.setInt(3, seat);

            statement.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void addSeat(Seat seat, int idSpec) {
        String sql = "INSERT INTO seats VALUES (NULL, ?, ?, ?, ?, ?, ?)";
        System.out.println(seat);
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, idSpec);
            statement.setInt(2, seat.getNumber());
            statement.setString(3, "false");
            statement.setInt(4, seat.getRow());
            statement.setDouble(5, seat.getPrice());
            statement.setString(6, seat.getType());

            statement.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
