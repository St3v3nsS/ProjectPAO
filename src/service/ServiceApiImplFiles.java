package service;

import enums.Genres;
import enums.MovieType;
import enums.PaymentType;
import exceptions.OccupiedSeatException;
import exceptions.PaymentTypeException;
import exceptions.TooManySeatsException;
import model.*;
import org.jetbrains.annotations.NotNull;
import repository.SpectaclesRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class ServiceApiImplFiles implements ServiceAPI {

    SpectaclesRepository spectaclesRepository = new SpectaclesRepository();
    Spectacle currentSpectacle;
    Scanner scanner = new Scanner(System.in);
    static long id = 0;
    Path moviePath = Paths.get("src/resources/movies.csv");
    Path theatrePath = Paths.get("src/resources/theatres.csv");
    Path writeCSV = Paths.get("src/resources/actions.csv");

    public ServiceApiImplFiles() {

        readMovies();
        readTheatre();

    }

    private void writeDataToCsv(@NotNull String data){

      try{
          Files.write(writeCSV, data.getBytes(), StandardOpenOption.APPEND);
      } catch (IOException ex){
          ex.printStackTrace();
      }

    }

    private void readTheatre() {
        try{
            Files.lines(theatrePath).skip(1).forEach(e ->{
                String[] values = e.split(", ");
                Spectacle spectacle = getSpectacle(values);

                Theatre theatre = new Theatre(spectacle.getName(), spectacle.getCast(), spectacle.getDuration(),
                        spectacle.getGenre(), spectacle.getLocation(), spectacle.getNrSeats(), spectacle.getNrVipSeats(),
                        new ArrayList<>(Arrays.asList(values[8].split("\\|"))), values[9]);

                spectaclesRepository.addSpectacle(theatre);
            });

        }catch (IOException exp){
            exp.printStackTrace();
        }
    }

    private void readMovies() {
        try{
            Files.lines(moviePath).skip(1).forEach(e->{
                String[] values = e.split(", ");
                Spectacle spectacle = getSpectacle(values);

                Movie movie = new Movie(spectacle.getName(), spectacle.getCast(), spectacle.getDuration(), spectacle.getGenre(),
                        spectacle.getLocation(),spectacle.getNrSeats(), spectacle.getNrVipSeats(), MovieType.valueOf(values[8]),
                        Double.parseDouble(values[9]));

                spectaclesRepository.addSpectacle(movie);
            });
        }catch (IOException exp){
            exp.printStackTrace();
        }
    }

    private Spectacle getSpectacle(@NotNull String[] values) {

        Spectacle spectacle = new Spectacle();
        spectacle.setName(values[1]);
        spectacle.setCast(new HashSet<>(Arrays.asList(values[2].split("\\| "))));
        spectacle.setDuration(values[3]);
        spectacle.setGenre(Genres.valueOf(values[4]));
        spectacle.setLocation(values[5]);
        spectacle.setNrSeats(Integer.parseInt(values[6]));
        spectacle.setNrVipSeats(Integer.parseInt(values[7]));

        return spectacle;
    }

    @Override
    public void displaySpectacles() {
        writeString("Display Spectacles");
        spectaclesRepository.findAll().forEach(System.out::println);
    }

    private void writeString(String data) {

        Date from = Date.from(Instant.now());

        try{
            Files.lines(writeCSV).skip(1).forEach(e -> id = Long.parseLong(e.split(", ")[0]));
        }
        catch (IOException exp){
            exp.printStackTrace();
        }
        id++;
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(", "+ data + ", ");
        sb.append(from.toString() + '\n');
        writeDataToCsv(sb.toString());
    }

    @Override
    public void selectSpectacle(int index) throws ArrayIndexOutOfBoundsException{
        writeString("Select Spectacle");
        currentSpectacle = spectaclesRepository.findByIndex(index);
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
        writeString("Add spectacle");
        spectaclesRepository.addSpectacle(new Movie("Aquamannnn", new HashSet<>(asList("Jason Mamoa", "Amber Heard", "Nicole Kidman")),
                "2h22", Genres.SCIFI, "Cinemacity MegasaMall", 15,10, MovieType.MOVIE4DX, 7.3));
    }

    @Override
    public void addClientForSpectacle(Client client) {
        writeString("Add Client for Spectacle");
        ArrayList<? extends Seat> seats = currentSpectacle.getSeats();
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            if(s.isOccupied()){
                throw new OccupiedSeatException("Seat already taken");
            }
            if(client instanceof VipClient){
                if(!s.getType().equals("Armchair")) {
                    throw new OccupiedSeatException("Seat not for VIP!");
                }
            }else{

                if(s.getType().equals("Armchair")){
                    throw new OccupiedSeatException("Seat only for VIP!");
                }
            }

        }
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            s.setOccupied(true);
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

    private void displaySeats(@NotNull List<? extends Seat> seats){
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

        writeString("Create Client");
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
        writeString("Exit");
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
        writeString("Number of spectacles");
        return spectaclesRepository.findAll().size();
    }
}
