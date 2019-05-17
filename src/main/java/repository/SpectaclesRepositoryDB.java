package repository;

import enums.Genres;
import enums.MovieType;
import model.Movie;
import model.Spectacle;
import model.Theatre;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SpectaclesRepositoryDB implements ISpectaclesRepository{
    private String url = "jdbc:mysql://localhost/cineJohn";
    private String user = "root";
    private String password = "";

    @Override
    public Spectacle findByName(String name) {
        Spectacle spectacle = null;
        String sql1 = "SELECT * FROM spectacles s, movies m WHERE s.id_movie = m.idmovies AND s.spectacle_name = ?";
        String sql2 = "SELECT * FROM spectacles s, theatres t WHERE s.id_theatre = t.idtheatres AND s.spectacle_name = ?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql2)){

            preparedStatement.setString(1, name);
            preparedStatement1.setString(1, name);
            ResultSet movies = preparedStatement.executeQuery();
            ResultSet theatres = preparedStatement1.executeQuery();

            while (movies.next()){
                String id = movies.getString("id_spec");
                Set<String> cast = Arrays.stream(movies.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = movies.getString("duration");
                Genres genre = Genres.valueOf(movies.getString("genre"));
                String location = movies.getString("location");
                int normalSeats = movies.getInt("normalSeats");
                int vipSeats = movies.getInt("vipSeats");
                MovieType type = MovieType.valueOf(movies.getString("type"));
                double imdbNote = Double.parseDouble(movies.getString("imdbNote"));

                spectacle = new Movie(id, name, cast, duration, genre, location, normalSeats, vipSeats, type, imdbNote);
            }

            while (theatres.next()){

                String id = theatres.getString("id_spec");
                Set<String> cast = Arrays.stream(theatres.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = theatres.getString("duration");
                Genres genre = Genres.valueOf(theatres.getString("genre"));
                String location = theatres.getString("location");
                int normalSeats = theatres.getInt("normalSeats");
                int vipSeats = theatres.getInt("vipSeats");
                List<String> scenery = Arrays.stream(theatres.getString("scenery").split("\\| "))
                        .collect(Collectors.toList());
                String author = theatres.getString("author");
                spectacle = new Theatre(id, name, cast, duration, genre, location, normalSeats, vipSeats, new ArrayList<>(scenery), author);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return spectacle;
    }

    @Override
    public Spectacle findByIndex(int index) {
        Spectacle spectacle = null;
        String sql1 = "SELECT * FROM spectacles s, movies m WHERE s.id_movie = m.idmovies AND s.id_spec = ?";
        String sql2 = "SELECT * FROM spectacles s, theatres t WHERE s.id_theatre = t.idtheatres AND s.id_spec = ?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql2)){

            preparedStatement.setInt(1, index);
            preparedStatement1.setInt(1, index);
            ResultSet movies = preparedStatement.executeQuery();
            ResultSet theatres = preparedStatement1.executeQuery();

            if (movies != null){
                while (movies.next()){
                    String id = movies.getString("id_spec");
                    String name = movies.getString("name");
                    Set<String> cast = Arrays.stream(movies.getString("cast").split("\\| "))
                            .collect(Collectors.toSet());
                    String duration = movies.getString("duration");
                    Genres genre = Genres.valueOf(movies.getString("genre"));
                    String location = movies.getString("location");
                    int normalSeats = movies.getInt("normalSeats");
                    int vipSeats = movies.getInt("vipSeats");
                    MovieType type = MovieType.valueOf(movies.getString("type"));
                    double imdbNote = Double.parseDouble(movies.getString("imdbNote"));

                    spectacle = new Movie(id, name, cast, duration, genre, location, normalSeats, vipSeats, type, imdbNote);
                }
            }

            if(theatres != null){
                while (theatres.next()){

                    String id = theatres.getString("id_spec");
                    String name = theatres.getString("name");
                    Set<String> cast = Arrays.stream(theatres.getString("cast").split("\\| "))
                            .collect(Collectors.toSet());
                    String duration = theatres.getString("duration");
                    Genres genre = Genres.valueOf(theatres.getString("genre"));
                    String location = theatres.getString("location");
                    int normalSeats = theatres.getInt("normalSeats");
                    int vipSeats = theatres.getInt("vipSeats");
                    List<String> scenery = Arrays.stream(theatres.getString("scenery").split("\\| "))
                            .collect(Collectors.toList());
                    String author = theatres.getString("author");
                    spectacle = new Theatre(id, name, cast, duration, genre, location, normalSeats, vipSeats, new ArrayList<>(scenery), author);
                }
            }


        }catch (SQLException e){
            e.printStackTrace();
        }

        return spectacle;
    }

    @Override
    public void addSpectacle(Spectacle spectacle) {
        if (spectacle instanceof Movie){
            String sql = "INSERT INTO movies VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sql2 = "INSERT INTO spectacles (id_movie, spectacle_name) SELECT idmovies, name FROM movies WHERE name = ?";
            String sql3 = "DELETE FROM movies_to_add WHERE name = ? ";

            try(Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql3)){
                StringBuilder cast = new StringBuilder();
                spectacle.getCast()
                        .forEach(e -> cast.append(e + "| "));
                cast.replace(cast.length() -2, cast.length(), "");
                preparedStatement.setString(1, spectacle.getName());
                preparedStatement.setString(2, cast.toString());
                preparedStatement.setString(3, spectacle.getDuration());
                preparedStatement.setString(4, spectacle.getGenre().name());
                preparedStatement.setString(5, spectacle.getLocation());
                preparedStatement.setInt(6, spectacle.getNrSeats());
                preparedStatement.setInt(7, spectacle.getNrVipSeats());
                preparedStatement.setString(8, ((Movie) spectacle).getType().name());
                preparedStatement.setDouble(9, ((Movie) spectacle).getImdbNote());
                preparedStatement.executeUpdate();

                preparedStatement1.setString(1, spectacle.getName());
                preparedStatement1.executeUpdate();

                preparedStatement2.setString(1, spectacle.getName());
                preparedStatement2.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        else{
            String sql = "INSERT INTO theatres VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sql2 = "INSERT INTO spectacles (id_theatre, spectacle_name) SELECT idtheatres, name FROM theatres WHERE name = ?";
            String sql3 = "DELETE FROM theatres_to_add WHERE name = ? ";
            try(Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql3)){
                StringBuilder cast = new StringBuilder();
                spectacle.getCast()
                        .forEach(e -> cast.append(e + "| "));
                cast.replace(cast.length() -2, cast.length(), "");

                StringBuilder scenery = new StringBuilder();
                ((Theatre) spectacle).getScenery().forEach(e -> scenery.append(e + "| "));
                scenery.replace(scenery.length()-2, scenery.length(), "");

                preparedStatement.setString(1, spectacle.getName());
                preparedStatement.setString(2, cast.toString());
                preparedStatement.setString(3, spectacle.getDuration());
                preparedStatement.setString(4, spectacle.getGenre().name());
                preparedStatement.setString(5, spectacle.getLocation());
                preparedStatement.setInt(6, spectacle.getNrSeats());
                preparedStatement.setInt(7, spectacle.getNrVipSeats());
                preparedStatement.setString(8, scenery.toString());
                preparedStatement.setString(9, ((Theatre) spectacle).getAuthor());
                preparedStatement.executeUpdate();

                preparedStatement1.setString(1, spectacle.getName());
                preparedStatement1.executeUpdate();

                preparedStatement2.setString(1, spectacle.getName());
                preparedStatement2.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Spectacle> findAll() {
        List<Spectacle> shows = new ArrayList<>();
        String sql1 = "SELECT * FROM spectacles s, movies m WHERE s.id_movie = m.idmovies";
        String sql2 = "SELECT * FROM spectacles s, theatres t WHERE s.id_theatre = t.idtheatres";


        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
            ResultSet movies = preparedStatement.executeQuery();
            ResultSet theatres = preparedStatement1.executeQuery();

        ){
            while (movies.next()){

                String id = movies.getString("idmovies");
                String name = movies.getString("name");
                Set<String> cast = Arrays.stream(movies.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = movies.getString("duration");
                Genres genre = Genres.valueOf(movies.getString("genre"));
                String location = movies.getString("location");
                int normalSeats = movies.getInt("normalSeats");
                int vipSeats = movies.getInt("vipSeats");
                MovieType type = MovieType.valueOf(movies.getString("type"));
                double imdbNote = Double.parseDouble(movies.getString("imdbNote"));

                Spectacle s = new Movie(id, name, cast, duration, genre, location, normalSeats, vipSeats, type, imdbNote);

                shows.add(s);
            }

            while (theatres.next()){

                String id = theatres.getString("idtheatres");
                String name = theatres.getString("name");
                Set<String> cast = Arrays.stream(theatres.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = theatres.getString("duration");
                Genres genre = Genres.valueOf(theatres.getString("genre"));
                String location = theatres.getString("location");
                int normalSeats = theatres.getInt("normalSeats");
                int vipSeats = theatres.getInt("vipSeats");
                List<String> scenery = Arrays.stream(theatres.getString("scenery").split("\\| "))
                        .collect(Collectors.toList());
                String author = theatres.getString("author");
                Spectacle s = new Theatre(id, name, cast, duration, genre, location, normalSeats, vipSeats, new ArrayList<>(scenery), author);
                shows.add(s);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return shows;

    }
}
