package service;

import domain.Client;
import domain.Movie;
import domain.Spectacle;
import domain.Theatre;
import enums.Genres;
import enums.MovieType;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;

public class ServiceApiImpl implements ServiceAPI{

    private Client currentClient;
    private Spectacle currentSpectacle;
    private ArrayList<Spectacle> spectacles;

    public ServiceApiImpl() {
        spectacles = new ArrayList<>();
        spectacles.add(new Movie("Bohemian Rhapsody", new ArrayList<>(asList("Rami Malek", "Luay Boynten", "Ben Hardy",
                "Mike Myers")), "2h13", Genres.DRAMA,
                "AFI IMAX", 15, 5, MovieType.IMAX, 8.1));
        spectacles.add(new Theatre("Bani din cer", new ArrayList<>(asList("Mihai Bendeac", "Mihaela Teleoaca", "Delia Natea")),
                "1h30", Genres.COMEDY, "Teatrul de comedie", 10, 5, new ArrayList<>(asList("geanta", "casa")), "Ray Cooney"));
    }

    @Override
    public void displaySpectacles() {
        spectacles.stream().forEach(System.out::println);
    }

    @Override
    public void selectSpectacle(int index) throws ArrayIndexOutOfBoundsException{
        currentSpectacle = spectacles.get(index);
        System.out.println("You choose: " + currentSpectacle.getName());
    }

    @Override
    public void showSeatsForSpectacle() {

        System.out.println("  Scene  \n");
        currentSpectacle.showSeats();
    }

    @Override
    public void addSpectacle() {
        spectacles.add(new Movie("Aquaman", new ArrayList<>(asList("Jason Mamoa", "Amber Heard", "Nicole Kidman")),
                "2h22", Genres.SCIFI, "Cinemacity MegaMall", 15,10, MovieType.MOVIE4DX, 7.3));
    }

    @Override
    public void addClientForSpectacle() {

    }

    @Override
    public void createClient() {

    }
}
