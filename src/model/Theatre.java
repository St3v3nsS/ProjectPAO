package model;

import enums.Genres;

import java.util.ArrayList;
import java.util.Set;

public class Theatre extends Spectacle {

    private ArrayList<String> scenery;
    private String author;

    public Theatre(String name, Set<String> cast, String duration, Genres genre, String location, int nrSeats, int nrVipSeats, ArrayList<String> scenery, String author) {
        super(name, cast, duration, genre, location, nrSeats, nrVipSeats);
        this.scenery = scenery;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Theatre{" +
                "scenery=" + scenery +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", cast=" + cast +
                ", duration='" + duration + '\'' +
                ", genre=" + genre +
                ", location='" + location + '\'' +
                '}';
    }

    public ArrayList<String> getScenery() {
        return scenery;
    }

    public void setScenery(ArrayList<String> scenery) {
        this.scenery = scenery;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
