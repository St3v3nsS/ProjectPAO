package service;

import enums.Genres;
import enums.MovieType;
import enums.PaymentType;
import exceptions.*;
import model.*;
import org.jetbrains.annotations.NotNull;
import repository.ClientsRepository;
import repository.SpectaclesRepository;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServiceApiImplFiles implements ServiceAPI {

    private static long id = 0;
    private static long idClient = 0;
    private static long idNewMovie = 1;
    private static long idNewTheatre = 1;
    private double toPay = 0.0;

    private SpectaclesRepository spectaclesRepository = new SpectaclesRepository();
    private ClientsRepository clientsRepository = new ClientsRepository();
    private Spectacle currentSpectacle;
    private Scanner scanner = new Scanner(System.in);
    private Path moviePath = Paths.get("src/resources/movies.csv");
    private Path theatrePath = Paths.get("src/resources/theatres.csv");
    private Path writeCSV = Paths.get("src/resources/actions.csv");
    private Path addMoviePath = Paths.get("src/resources/movies_to_add.csv");
    private Path addTheatrePath = Paths.get("src/resources/theatres_to_add.csv");
    private Path clientPath = Paths.get("src/resources/clients.csv");


    public ServiceApiImplFiles() {

        readMovies();
        readTheatre();
        readClients();

    }

    private void readClients() {
        try{
            Files.lines(clientPath).skip(1).forEach( e ->{
                String[] values = e.split(", ");
                Client client = getClient(values);

                clientsRepository.addClient(client);
                currentSpectacle = spectaclesRepository.findByName(client.getSpectacle());
                addClientForSpectacle(client);
            });
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private Client getClient(String[] values) {
        Client client;
        if(values[5].equals("true")){
            client = new VipClient();
        }else{
            client = new Client();
        }
        client.setName(values[1]);
        String[] seats = values[2].split("\\| ");
        List<Integer> lista = Arrays.stream(seats).flatMapToInt(e -> IntStream.of(Integer.parseInt(e)))
                .boxed()
                .collect(Collectors.toList());
        client.setSeats(new ArrayList<>(lista));
        client.setPaymentType(PaymentType.valueOf(values[3]));
        client.setSpectacle(values[4]);

        return client;
    }

    private void writeDataToCsv(@NotNull String data, Path path){

      try{
          Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
      } catch (IOException ex){
          ex.printStackTrace();
      }

    }

    private void readTheatre() {
        try{
            Files.lines(theatrePath).skip(1).forEach(e ->{
                String[] values = e.split(", ");
                Spectacle spectacle = getSpectacle(values);

                Theatre theatre = new Theatre(spectacle.getId(), spectacle.getName(), spectacle.getCast(),
                        spectacle.getDuration(), spectacle.getGenre(), spectacle.getLocation(), spectacle.getNrSeats(),
                        spectacle.getNrVipSeats(), new ArrayList<>(Arrays.asList(values[8].split("\\|"))), values[9]);

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

                Movie movie = new Movie(spectacle.getId(), spectacle.getName(), spectacle.getCast(), spectacle.getDuration(), spectacle.getGenre(),
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
        spectacle.setId(spectaclesRepository.findAll().size() + 1 + "");
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
        writeString("Choose spectacle");
        spectaclesRepository.findAll().forEach(System.out::println);
    }

    public void writeString(String data) {

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
        writeDataToCsv(sb.toString(), writeCSV);
    }

    @Override
    public void selectSpectacle(int index) throws ArrayIndexOutOfBoundsException{
        writeString("Select spectacle");
        currentSpectacle = spectaclesRepository.findByIndex(index - 1);
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
        System.out.println("Do you want to add Movie or Theatre? Please type M/T");
        String type = scanner.nextLine();
        if("M".equals(type.toUpperCase())){
            // Read A movie
            try{
                Files.lines(addMoviePath).skip(idNewMovie).limit(1).forEach(e -> {
                    String[] values = e.split(", ");
                    Spectacle spectacle = getSpectacle(values);

                    Movie movie = new Movie(spectacle.getId(), spectacle.getName(), spectacle.getCast(), spectacle.getDuration(), spectacle.getGenre(),
                            spectacle.getLocation(),spectacle.getNrSeats(), spectacle.getNrVipSeats(), MovieType.valueOf(values[8]),
                            Double.parseDouble(values[9]));

                    spectaclesRepository.addSpectacle(movie);
                    idNewMovie++;

                });
            }catch (IOException ex){
                ex.printStackTrace();
            }

        }
        else if("T".equals(type.toUpperCase())){
            // Read a theatre
            try {
                Files.lines(addTheatrePath).skip(idNewTheatre).limit(1).forEach(e ->{
                    String [] values = e.split(", ");
                    Spectacle spectacle = getSpectacle(values);

                    Theatre theatre = new Theatre(spectacle.getId(), spectacle.getName(), spectacle.getCast(),
                            spectacle.getDuration(), spectacle.getGenre(), spectacle.getLocation(), spectacle.getNrSeats(),
                            spectacle.getNrVipSeats(), new ArrayList<>(Arrays.asList(values[8].split("\\|"))), values[9]);

                    spectaclesRepository.addSpectacle(theatre);
                    idNewTheatre++;
                });
            }catch (IOException exp){
                exp.printStackTrace();
            }

        }else {
            throw new RuntimeException("Wrong type!");
        }
    }

    @Override
    public void addClientForSpectacle(Client client) {
        writeString("Add client for spectacle");
        List<Seat> seats = currentSpectacle.getSeats();
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
        return spectaclesRepository.findByIndex(index).getSeats();
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
        writeDataToCsv(clientToString(client), clientPath);

        return client;
    }

    @Override
    public double getTotalToPay() {
        writeString("Get total to pay for spectacle");
        return toPay;
    }

    @Override
    public List<Movie> getNextMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            Files.lines(addMoviePath).skip(1).forEach(e -> {
                String[] values = e.split(", ");
                Spectacle spectacle = getSpectacle(values);

                Movie movie = new Movie(spectacle.getId(), spectacle.getName(), spectacle.getCast(), spectacle.getDuration(), spectacle.getGenre(),
                        spectacle.getLocation(),spectacle.getNrSeats(), spectacle.getNrVipSeats(), MovieType.valueOf(values[8]),
                        Double.parseDouble(values[9]));

                try{
                    spectaclesRepository.findByName(spectacle.getName());
                }catch (SpectacleNotFoundException ex){
                    movies.add(movie);
                }

            });
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return movies;
    }

    @Override
    public List<Theatre> getNextTheatres() {
        ArrayList<Theatre> theatres = new ArrayList<>();

        try {
            Files.lines(addTheatrePath).skip(1).forEach(e ->{
                String [] values = e.split(", ");
                Spectacle spectacle = getSpectacle(values);

                Theatre theatre = new Theatre(spectacle.getId(), spectacle.getName(), spectacle.getCast(),
                        spectacle.getDuration(), spectacle.getGenre(), spectacle.getLocation(), spectacle.getNrSeats(),
                        spectacle.getNrVipSeats(), new ArrayList<>(Arrays.asList(values[8].split("\\|"))), values[9]);

                try{
                    spectaclesRepository.findByName(spectacle.getName());
                }catch (SpectacleNotFoundException ex){
                    theatres.add(theatre);
                }
            });
        }catch (IOException exp){
            exp.printStackTrace();
        }

        return theatres;

    }

    @Override
    public void addSpectacle(Spectacle spectacle) {
        spectaclesRepository.addSpectacle(spectacle);
    }
}
