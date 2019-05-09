import model.Client;
import service.ServiceAPI;
import service.ServiceApiImplFiles;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ServiceAPI serviceAPI = new ServiceApiImplFiles();
        ArrayList<Client> clients = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int value = 0;

        App myApp = new App();

        while (value != 9){
            System.out.println("\t\tMENIU\n" +
                    "1. Display spectacles\n" +
                    "2. Choose a spectacle\n" +
                    "3. Display seats for spectacle\n" +
                    "4. Add new client\n" +
                    "5. Show amount to pay\n" +
                    "6. Sort new clients\n" +
                    "7. Add new spectacle\n" +
                    "8. Show total number of spectacles\n"+
                    "9. Exit\n");
            value = scanner.nextInt();scanner.nextLine();
            switch (value){
                case 1:
                    serviceAPI.displaySpectacles();
                    break;
                case 2:
                    System.out.println("Enter id: ");
                    int index = scanner.nextInt(); scanner.nextLine();

                    if(index >= 0 && index < serviceAPI.numberOfSpectacles()){
                        serviceAPI.selectSpectacle(index);
                        System.out.println();
                    }else{
                        System.out.println("Sorry, wrong number! Bye... ");
                    }
                    break;
                case 3:
                    serviceAPI.showSeatsForSpectacle();
                    break;
                case 4:
                    clients.add(serviceAPI.createClient());
                    break;
                case 5:
                    serviceAPI.writeString("Show amount to pay");
                    serviceAPI.printTotalToPay();
                    break;
                case 6:
                    serviceAPI.writeString("Sorting new clients");
                    Collections.sort(clients);
                    System.out.println("New clients sorted alphabetically: " + clients);
                    break;
                case 7:
                    serviceAPI.writeString("Add new spectacle");
                    System.out.println("Login into Admin...\nEnter password for admin (No security purposes): ");
                    String psw = scanner.nextLine();
                    if (psw.equals("admin")){

                        serviceAPI.addSpectacle();
                    }else {
                        System.out.println(ServiceAPI.ANSI_RED + "Wrong password! Good luck next time!" + ServiceAPI.ANSI_RESET);
                    }
                    break;
                case 8:
                    serviceAPI.writeString("Show number of spectacles");
                    System.out.println("There are a total of " + serviceAPI.numberOfSpectacles() + " sectacles today!");
                    break;
                case 9:
                    serviceAPI.writeString("Exit");
                    serviceAPI.exitApp();
                    break;
            }

        }


    }
}
