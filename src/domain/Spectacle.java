package domain;

import enums.Genres;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static service.ServiceAPI.ANSI_GREEN;
import static service.ServiceAPI.ANSI_RED;
import static service.ServiceAPI.ANSI_RESET;

public class Spectacle {

    protected String name;
    protected Set<String> cast;
    protected String duration;
    protected Genres genre;
    protected String location;
    protected int nrSeats;
    protected int nrVipSeats;
    protected ArrayList<Seat> seats;


    public Spectacle() {
    }

    public Spectacle(String name, Set<String> cast, String duration, Genres genre, String location, int nrSeats, int nrVipSeats) {
        this.name = name;
        this.cast = cast;
        this.duration = duration;
        this.genre = genre;
        this.location = location;
        this.nrSeats = nrSeats;
        this.nrVipSeats = nrVipSeats;

        int cnt = 1;
        seats = new ArrayList<>();
        for(int i = 0; i < nrSeats/5 ; i++){
            for(int j = 0; j < 5; j++)
                seats.add(new Seat(cnt++, false, i+1, 15));
        }


        for(int i = 0; i < nrVipSeats; i++){
            seats.add(new VipSeat(cnt++, false, nrSeats/5 + 1, 15, 0.3));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNrSeats() {
        return nrSeats;
    }

    public void setNrSeats(int nrSeats) {
        this.nrSeats = nrSeats;
    }

    public int getNrVipSeats() {
        return nrVipSeats;
    }

    public void setNrVipSeats(int nrVipSeats) {
        this.nrVipSeats = nrVipSeats;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Spectacle{" +
                "name='" + name + '\'' +
                ", cast=" + cast +
                ", duration='" + duration + '\'' +
                ", genre=" + genre +
                ", location='" + location + '\'' +
                '}';
    }

    public void showSeats(){
        for(int j = 0; j < seats.size(); j+= 5){
            for(int i = 0; i <= 4; i++){
                if (seats.get(j+i).isOccupied())
                    System.out.print(ANSI_RED + seats.get(j+i).number + " ");
                else System.out.print(ANSI_GREEN + seats.get(j+i).number + " ");
            }
            System.out.println(ANSI_RESET);
        }
    }
}
