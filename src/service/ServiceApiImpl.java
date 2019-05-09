package service;

import exceptions.NoNameException;
import model.*;
import enums.Genres;
import enums.MovieType;
import enums.PaymentType;
import exceptions.OccupiedSeatException;
import exceptions.PaymentTypeException;
import exceptions.TooManySeatsException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;

public class ServiceApiImpl implements ServiceAPI{

    private Client currentClient;
    private Spectacle currentSpectacle;
    private List<Spectacle> spectacles;
    private Scanner scanner = new Scanner(System.in);
    private double toPay;

    public ServiceApiImpl() {
        spectacles = new ArrayList<>();
        spectacles.add(new Movie("1", "Bohemian Rhapsody", new HashSet<>(asList("Rami Malek", "Luay Boynten", "Ben Hardy",
                "Mike Myers")), "2h13", Genres.DRAMA,
                "AFI IMAX", 15, 5, MovieType.IMAX, 8.1));
        spectacles.add(new Theatre("2", "Bani din cer",
                new HashSet<>(asList("Mihai Bendeac", "Mihaela Teleoaca", "Delia Natea")), "1h30", Genres.COMEDY, "Teatrul de comedie", 10, 5, new ArrayList<>(asList("geanta", "casa")), "Ray Cooney"));
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
        writeString("Show seats for spectacle");
        System.out.println("  Scene  \n");
        currentSpectacle.showSeats();
    }

    @Override
    public void addSpectacle() {
        spectacles.add(new Movie("3", "Aquaman", new HashSet<>(asList("Jason Mamoa", "Amber Heard", "Nicole Kidman")),
                "2h22", Genres.SCIFI, "Cinemacity MegaMall", 15,10, MovieType.MOVIE4DX, 7.3 ));
    }

    @Override
    public void addClientForSpectacle(Client client) throws OccupiedSeatException {
        List<Seat> seats = currentSpectacle.getSeats();
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            if(s.isOccupied()){
                throw new OccupiedSeatException("Seat already taken");
            }
            if(client instanceof VipClient){
                System.out.println("NOT");
                if(!s.getType().equals("Armchair")) {
                    throw new OccupiedSeatException("Seat not for VIP!");
                }
            }else{

                if(s.getType().equals("Armchair")){
                    throw new OccupiedSeatException("Seat only for VIP!");
                }
            }

        }

        toPay = 0.0;
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            s.setOccupied(true);
            toPay += s.getPrice();
        }

        showSeatsForSpectacle();

    }

    private ArrayList<Integer> getSeats(boolean isVip) throws TooManySeatsException {
        System.out.println("Please enter a number of tickets: ");
        long nrSeats = scanner.nextInt();
        if(nrSeats > getNumberSeats(isVip)){
            throw new TooManySeatsException("Too many seats!");
        }
        System.out.println("Enter the seats number: ");
        ArrayList<Integer> seats= new ArrayList<>();
        for(int i = 0; i < nrSeats; i++){
            int value = scanner.nextInt();
            seats.add(value);
        }
        scanner.nextLine();
        return seats;

    }

    private long getNumberSeats(boolean isVip){
        if(isVip){
            return currentSpectacle.getSeats()
                    .stream()
                    .filter(e -> e.getType().equals("Armchair"))
                    .filter(e -> !e.isOccupied())
                    .count();
        }
        else{
            return currentSpectacle.getSeats()
                    .stream()
                    .filter(e -> !e.getType().equals("Armchair"))
                    .filter(e -> !e.isOccupied())
                    .count();
        }
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

    private PaymentType getPaymentType() throws PaymentTypeException {

        System.out.println("What is your payment method? Card/Cash");
        String payment = scanner.nextLine().toUpperCase();
        switch (payment) {
            case "CARD":
                return PaymentType.CARD;
            case "CASH":
                return PaymentType.CASH;
            default:
                throw new PaymentTypeException("Wrong Payment Type! Type Card or Cash");
        }

    }

    @Override
    public Client createClient() {
        Client client = null;


        String name, vip, yesNo;
        ArrayList<Integer> seats;
        int maxTries = 3;
        int count = 0;
        boolean isVip;

        System.out.println("Please enter your name: ");
        name = scanner.nextLine();
        System.out.println("Do you want to be VIP? Y/N ");
        vip = scanner.nextLine().toUpperCase();
        if(vip.equals("Y")){
            isVip = true;
            displaySeats(currentSpectacle.getSeats()
                    .stream()
                    .filter(e -> e.getType().equals("Armchair"))
                    .collect(Collectors.toList()));

            int maxGetSeats = 3;
            int countMax = 1;
            while(true){
                try {
                    seats = getSeats(isVip);
                    client = new VipClient(name, seats);



                    break;
                }catch (TooManySeatsException exception){
                    System.err.println(exception.getMessage());
                    if(countMax == maxGetSeats){
                        System.out.println("Too many tries!");
                        break;
                    }
                    else countMax++;
                }
            }

        }
        else{
            displaySeats(currentSpectacle.getSeats()
                    .stream()
                    .filter(e -> !e.getType().equals("Armchair"))
                    .collect(Collectors.toList()));
            isVip = false;
            int maxGetSeats = 3;
            int countMax = 1;
            while(true){
                try {
                    seats = getSeats(isVip);
                    client = new Client(name, seats);
                    client.setToPay(toPay);
                    PaymentType paymentType;
                    int payCount = 1;
                    int maxPayCount = 3;
                    while(true){
                        try{
                            paymentType = getPaymentType();
                            client.setPaymentType(paymentType);
                            break;
                        }catch (PaymentTypeException paymentException){
                            System.err.println(paymentException.getMessage());
                            if(payCount == maxPayCount) break;
                            else payCount++;
                        }
                    }
                    break;
                }catch (TooManySeatsException exception){
                    System.err.println(exception.getMessage());
                    if(countMax == maxGetSeats){
                        System.out.println("Too many tries!");
                        break;
                    }
                    else countMax++;
                }
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

                    try{
                        client.setSeats(getSeats(isVip));
                    }
                    catch (TooManySeatsException ex){
                        System.err.println(ex.getMessage());
                    }
                    if(count == maxTries - 1){
                        System.out.println("You are blind? Don't you see that red are occupied?");
                        return null;
                    }
                    count++;
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
    public void writeString(String data) {
        System.out.println("You cannot use this method!");
    }

    @Override
    public int numberOfSpectacles() {
        return spectacles.size();
    }

    @Override
    public List<String> getSpectaclesName() {
        return null;
    }

    @Override
    public Spectacle getSelectedSpectacle(int index) {
        return null;
    }

    @Override
    public void printTotalToPay() {
        System.out.println(currentClient.getToPay());
    }

    @Override
    public List<Seat> getSpectacleSeats(int index) {
        return null;
    }

    @Override
    public Client createClient(String name, String vip, List<Integer> selectedSeats) throws NoNameException, TooManySeatsException, OccupiedSeatException {
        return null;
    }

    @Override
    public double getTotalToPay() {
        return 0;
    }
}
