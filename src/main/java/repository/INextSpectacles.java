package repository;

import model.Movie;
import model.Spectacle;
import model.Theatre;

import java.util.List;

public interface INextSpectacles {

    List<Movie> findAllMovies();
    List<Theatre> findAllTheatres();
}
