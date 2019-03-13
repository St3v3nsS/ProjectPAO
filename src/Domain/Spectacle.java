package Domain;

import java.util.List;

public class Spectacle {

    protected String name;
    protected List<String> cast;
    protected String duration;
    protected Genres genre;

    public Spectacle(String name, List<String> cast, String duration, Genres genre) {
        this.name = name;
        this.cast = cast;
        this.duration = duration;
        this.genre = genre;
    }

    public Spectacle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }
}
