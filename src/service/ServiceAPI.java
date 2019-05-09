package service;

import exceptions.NoNameException;
import exceptions.OccupiedSeatException;
import exceptions.TooManySeatsException;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface ServiceAPI {

    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_RESET = "\u001B[0m";

    void displaySpectacles();
    void selectSpectacle(int index);
    void showSeatsForSpectacle();
    void addSpectacle();
    void printTotalToPay();
    void addClientForSpectacle(Client client);
    void exitApp();
    Client createClient();
    void writeString(String data);
    int numberOfSpectacles();
    List<String> getSpectaclesName();
    Spectacle getSelectedSpectacle(int index);
    List<Seat> getSpectacleSeats(int index);
    Client createClient(String name, String vip, List<Integer> selectedSeats) throws NoNameException, TooManySeatsException, OccupiedSeatException;
    double getTotalToPay();
    List<Movie> getNextMovies();
    List<Theatre> getNextTheatres();
    void addSpectacle(Spectacle spectacle);
}
