package repository;

import enums.Genres;
import enums.MovieType;
import model.Movie;
import model.Spectacle;
import model.Theatre;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NextSpectaclesDB implements INextSpectacles {

    private String url = "jdbc:mysql://localhost/cineJohn";
    private String user = "root";
    private String password = "";


    @Override
    public List<Movie> findAllMovies() {
        List<Movie>  movies = new ArrayList<>();

        String sql = "SELECT * FROM movies_to_add";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet moviesResp = preparedStatement.executeQuery();){

            while (moviesResp.next()){
                String id = moviesResp.getString("idmovies");
                String name = moviesResp.getString("name");
                Set<String> cast = Arrays.stream(moviesResp.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = moviesResp.getString("duration");
                Genres genre = Genres.valueOf(moviesResp.getString("genre"));
                String location = moviesResp.getString("location");
                int normalSeats = moviesResp.getInt("normalSeats");
                int vipSeats = moviesResp.getInt("vipSeats");
                MovieType type = MovieType.valueOf(moviesResp.getString("type"));
                double imdbNote = Double.parseDouble(moviesResp.getString("imdbNote"));

                Movie s = new Movie(id, name, cast, duration, genre, location, normalSeats, vipSeats, type, imdbNote);

                movies.add(s);
            }
        }catch (SQLException exp){
            exp.printStackTrace();
        }

        return movies;

    }

    @Override
    public List<Theatre> findAllTheatres() {
        List<Theatre>  theatres = new ArrayList<>();

        String sql = "SELECT * FROM theatres_to_add";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet theatresResp = preparedStatement.executeQuery();){

            while (theatresResp.next()){

                String id = theatresResp.getString("idtheatres");
                String name = theatresResp.getString("name");
                Set<String> cast = Arrays.stream(theatresResp.getString("cast").split("\\| "))
                        .collect(Collectors.toSet());
                String duration = theatresResp.getString("duration");
                Genres genre = Genres.valueOf(theatresResp.getString("genre"));
                String location = theatresResp.getString("location");
                int normalSeats = theatresResp.getInt("normalSeats");
                int vipSeats = theatresResp.getInt("vipSeats");
                List<String> scenery = Arrays.stream(theatresResp.getString("scenery").split("\\| "))
                        .collect(Collectors.toList());
                String author = theatresResp.getString("author");
                Theatre s = new Theatre(id, name, cast, duration, genre, location, normalSeats, vipSeats, new ArrayList<>(scenery), author);

                theatres.add(s);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return theatres;
    }
}
