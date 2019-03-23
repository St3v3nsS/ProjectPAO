package service;

import domain.*;
import enums.Genres;
import enums.MovieType;
import enums.PaymentType;
import exceptions.OccupiedSeatException;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;

public class ServiceApiImpl implements ServiceAPI{

    private Client currentClient;
    private Spectacle currentSpectacle;
    private ArrayList<Spectacle> spectacles;
    Scanner scanner = new Scanner(System.in);

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
    public void addClientForSpectacle(Client client) throws OccupiedSeatException {
        ArrayList<? extends Seat> seats = currentSpectacle.getSeats();
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            if(s.isOccupied()){
                throw new OccupiedSeatException("Seat already taken");
            }
            s.setOccupied(true);

        }
        showSeatsForSpectacle();

    }

    private ArrayList<Integer> getSeats(){
        System.out.println("Please enter a number of tickets: ");
        int nrSeats = scanner.nextInt();
        System.out.println("Enter the seats number: ");
        ArrayList<Integer> seats= new ArrayList<>();
        for(int i = 0; i < nrSeats; i++){
            int value = scanner.nextInt();
            seats.add(value);
        }
        scanner.nextLine();
        return seats;

    }

    private void displaySeats(List<? extends Seat> seats){
        for(int j = 0; j < seats.size(); j+= 5){
            for(int i = 0; i <= 4; i++){
                if (seats.get(j+i).isOccupied())
                    System.out.print(ANSI_RED + seats.get(j+i).getNumber() + " ");
                else System.out.print(ANSI_GREEN + seats.get(j+i).getNumber() + " ");
            }
            System.out.println(ANSI_RESET);
        }
    }

    @Override
    public Client createClient() {
        Client client = null;
        String name, vip, yesNo;
        ArrayList<Integer> seats;
        int maxTries = 3;
        int count = 0;

        System.out.println("Please enter your name: ");
        name = scanner.nextLine();
        System.out.println("Do you want to be VIP? Y/N ");
        vip = scanner.nextLine().toUpperCase();
        if(vip.equals("Y")){

            displaySeats(currentSpectacle.getSeats().stream().filter(e -> e.getType().equals("Armchair")).collect(Collectors.toList()));
            seats = getSeats();
            client = new VipClient(name, seats);
        }
        else{
            displaySeats(currentSpectacle.getSeats().stream().filter(e -> !e.getType().equals("Armchair")).collect(Collectors.toList()));
            seats = getSeats();
            System.out.println("What is your payment method? Card/Cash");
            String payment = scanner.nextLine().toUpperCase();
            client = new Client(name, seats);
            if(payment.equals("CARD")){
                client.setPaymentType(PaymentType.CARD);
            }
            else{
                client.setPaymentType(PaymentType.CASH);
            }


        }
        System.out.println("Do you want to save the registration? Y/N ");
        yesNo = scanner.nextLine().toUpperCase();
        if(yesNo.equals("Y")){
            while(true){
                try{
                    addClientForSpectacle(client);
                    return client;
                }
                catch (OccupiedSeatException exception){
                    System.out.println(exception.getMessage());
                    count++;
                    client.setSeats(getSeats());
                    if(count == maxTries - 1){
                        System.out.println("You are blind? Don't you see that red are occupied?");
                        return null;
                    }
                }
            }


        }
        else{
            exitApp();
        }

        return client;

    }

    @Override
    public void exitApp() {
        String yes;
        System.out.println("Are you sure you want to leave? Y/N");
        yes = scanner.nextLine().toUpperCase();
        if(yes.equals("Y")){
            System.out.println("Leaving... ");
            System.exit(0);
        }
        else{
            createClient();
        }
    }

    @Override
    public int numberOfSpectacles() {
        return spectacles.size();
    }
}
