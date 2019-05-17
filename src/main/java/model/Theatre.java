package model;

import enums.Genres;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Theatre extends Spectacle {

    private List<String> scenery;
    private String author;

    public Theatre(String id, String name, Set<String> cast, String duration, Genres genre, String location, int nrSeats, int nrVipSeats, ArrayList<String> scenery, String author) {
        super(id,name, cast, duration, genre, location, nrSeats, nrVipSeats);
        this.scenery = scenery;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Theatre{" +
                "id="+id +
                ", name='" + name + '\'' +
                ", cast=" + cast +
                ", duration='" + duration + '\'' +
                ", genre=" + genre +
                ", location='" + location + '\'' +
                ", scenery=" + scenery +
                ", author='" + author + '\'' +
                '}';
    }

    public List<String> getScenery() {
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
