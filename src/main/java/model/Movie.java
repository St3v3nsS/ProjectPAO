package model;

import enums.Genres;
import enums.MovieType;

import java.util.Set;

public class Movie extends Spectacle {

    private MovieType type;
    private double imdbNote;

    public Movie(String id, String name, Set<String> cast, String duration, Genres genre, String location, int nrSeats, int nrVipSeats, MovieType type, double imdbNote) {
        super(id, name, cast, duration, genre, location, nrSeats, nrVipSeats);
        this.type = type;
        this.imdbNote = imdbNote;
    }

    public double getImdbNote() {
        return imdbNote;
    }

    public void setImdbNote(double imdbNote) {
        this.imdbNote = imdbNote;
    }

    public MovieType getType() {
        return type;
    }

    public void setType(MovieType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cast=" + cast +
                ", type=" + type +
                ", genre=" + genre +
                ", duration='" + duration + '\'' +
                ", location='" + location + '\'' +
                ", imdbNote=" + imdbNote +
                '}';
    }


}
