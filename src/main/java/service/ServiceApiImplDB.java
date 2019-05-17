package service;

import enums.PaymentType;
import exceptions.*;
import model.*;
import org.jetbrains.annotations.NotNull;
import repository.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ServiceApiImplDB implements ServiceAPI {

    private static long id = 0;
    private static long idClient = 0;
    private static long idNewMovie = 1;
    private static long idNewTheatre = 1;
    private double toPay = 0.0;

    private ISpectaclesRepository spectaclesRepository = new SpectaclesRepositoryDB();
    private IClientsRepository clientsRepository = new ClientsRepositoryDB();
    private INextSpectacles nextSpectaclesRepository = new NextSpectaclesDB();
    private ISeatRepository seatRepository = new SeatRepository();
    private Spectacle currentSpectacle;

    private Scanner scanner = new Scanner(System.in);
    private Path writeCSV = Paths.get("src/main/resources/actions.csv");
    private Path clientPath = Paths.get("src/main/resources/clients.csv");

    public ServiceApiImplDB() {

        readClients();

    }

    private void readClients() {
        clientsRepository.findAll()
                .forEach(client -> {
                    currentSpectacle = spectaclesRepository.findByName(client.getSpectacle());
                    client.getSeats().forEach(e -> {
                        seatRepository.unsetOccupied(e, Integer.parseInt(currentSpectacle.getId()));
                    });
                    try {
                        addClientForSpectacle(client);
                    }catch (RuntimeException ex){
                        ex.printStackTrace();
                    }
                });
    }

    private void writeDataToCsv(@NotNull String data, Path path){

        try{
            Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void displaySpectacles() {
        writeString("Choose spectacle");
        spectaclesRepository.findAll().forEach(System.out::println);
    }

    public void writeString(String data) {

        Date from = Date.from(Instant.now());

        try{
            Files.lines(writeCSV).skip(1).forEach(e -> id = Long.parseLong(e.split(",")[0]));
        }
        catch (IOException exp){
            exp.printStackTrace();
        }
        id++;
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(","+ data + ",");
        sb.append(from.toString() + "," + Thread.currentThread().getName() + '\n');
        writeDataToCsv(sb.toString(), writeCSV);
    }

    @Override
    public void selectSpectacle(int index) throws ArrayIndexOutOfBoundsException{
        writeString("Select spectacle");
        System.out.println(index);
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

    }

    @Override
    public void addClientForSpectacle(Client client) {
        writeString("Add client for spectacle");
        List<Seat> seats = seatRepository.getSeatsForSpectacle(Integer.parseInt(currentSpectacle.getId()));
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

        toPay = 0.0;
        for(Integer integer : client.getSeats()){
            Seat s = seats.get(integer - 1);
            s.setOccupied(true);
            seatRepository.updateOccupied(integer, Integer.parseInt(currentSpectacle.getId()));
            toPay += s.getPrice();
        }
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

        Client client = null;
        writeString("Create Client");

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
                    client.setToPay(toPay);
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

                    client.setToPay(toPay);

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
                    client.setSpectacle(currentSpectacle.getName());
                    writeDataToCsv(clientToString(client), clientPath);
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

    private String clientToString(Client client) {
        StringBuilder sb = new StringBuilder();
        try{
            Files.lines(clientPath).skip(1).forEach(e -> idClient = Long.parseLong(e.split(", ")[0]));
        }
        catch (IOException exp){
            exp.printStackTrace();
        }
        idClient++;

        sb.append(idClient + ", ");
        sb.append(client.getName()+ ", ");

        client.getSeats().forEach(e -> sb.append(e + "| "));
        sb.delete(sb.length()-2, sb.length());
        sb.append(", " + client.getPaymentType() + ", " + client.getSpectacle());

        if(client instanceof VipClient){
            sb.append(", true\n");
        }
        else{
            sb.append(", false\n");
        }

        return sb.toString();
    }


    @Override
    public List<String> getSpectaclesName() {
        writeString("Get spectacles name");
        ArrayList<String> specsName = new ArrayList<>();
        spectaclesRepository.findAll().forEach(e->{
            specsName.add(e.getName());
        });

        return specsName;
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
        return spectaclesRepository.findAll().size();
    }

    @Override
    public void printTotalToPay() {
        System.out.println("Last client have to pay: " + toPay);
    }

    @Override
    public Spectacle getSelectedSpectacle(int index) {
        writeString("Get selected spectacle");
        return spectaclesRepository.findByIndex(index);
    }

    @Override
    public List<Seat> getSpectacleSeats(int index) {
        writeString("Get spectacle seats");
        return seatRepository.getSeatsForSpectacle(index);
    }

    private ArrayList<Integer> getSelectedSeats(ArrayList<Integer> seats, boolean isVip) throws TooManySeatsException{

        int nrSeats = seats.size();

        if(nrSeats > getNumberSeats(isVip)){
            throw new TooManySeatsException("Too many seats!");
        }


        return seats;

    }

    @Override
    public Client createClient(String name, String vip, List<Integer> selectedSeats) throws NoNameException, TooManySeatsException, OccupiedSeatException {
        writeString("Create Client");

        Client client = null;
        ArrayList<Integer> seats;

        if(name == null){
            throw new NoNameException("Null name");
        }

        if (vip.equals("YES")){

            seats = getSelectedSeats(new ArrayList<>(selectedSeats), true);
            client = new VipClient(name, seats);

        } else {

            seats = getSelectedSeats(new ArrayList<>(selectedSeats), false);
            client = new Client(name, seats);
        }

        addClientForSpectacle(client);
        client.setSpectacle(currentSpectacle.getName());
        clientsRepository.addClient(client);

        return client;
    }

    @Override
    public double getTotalToPay(List<Integer> seats) {
        writeString("Get total to pay for spectacle");
        AtomicReference<Double> toPayy = new AtomicReference<>(0.0);
        List<Seat> seatList = seatRepository.getSeatsForSpectacle(Integer.parseInt(currentSpectacle.getId()));
        seats.forEach(e -> {
            System.out.println(seatList.get(e-1).getPrice());
            System.out.println(currentSpectacle.getName());
            toPayy.updateAndGet(v -> (double) (v + seatList.get(e - 1).getPrice()));
        });
        System.out.println(toPayy);
        return toPayy.get();
    }

    @Override
    public List<Movie> getNextMovies() {
       return nextSpectaclesRepository.findAllMovies();
    }

    @Override
    public List<Theatre> getNextTheatres() {
       return nextSpectaclesRepository.findAllTheatres();
    }

    @Override
    public void addSpectacle(Spectacle spectacle) {
        spectaclesRepository.addSpectacle(spectacle);
        int id = Integer.parseInt(spectacle.getId());
        List<Seat> seats = spectacle.getSeats();
        seats.forEach(e ->{
            System.out.println(e + " id");
             seatRepository.addSeat(e, id);
        });
    }

    @Override
    public List<Client> displayClients() {
        return clientsRepository.findAll();
    }

    @Override
    public void deleteClient(String name) {
        Client client = clientsRepository.findByName(name);
        System.out.println(client.getName());
        clientsRepository.deleteClient(client);
    }
}
