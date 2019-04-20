import model.Client;
import service.ServiceAPI;
import service.ServiceApiImpl;
import service.ServiceApiImplFiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ServiceAPI serviceAPI = new ServiceApiImplFiles();
        ArrayList<Client> clients = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        serviceAPI.displaySpectacles();

        System.out.println("Choose a spectacle");
        int index = scanner.nextInt(); scanner.nextLine();

        if(index >= 0 && index < serviceAPI.numberOfSpectacles()){
            serviceAPI.selectSpectacle(index);
            System.out.println();

            clients.add(serviceAPI.createClient());
            clients.add(serviceAPI.createClient());

            Collections.sort(clients);
            System.out.println("Sorted clients alphabetically: ");
            System.out.println(clients);
        }else{
            System.out.println("Sorry, wrong number! Bye... ");
        }


    }
}
