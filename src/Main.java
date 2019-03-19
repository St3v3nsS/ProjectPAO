import service.ServiceAPI;
import service.ServiceApiImpl;

public class Main {

    public static void main(String[] args) {
        ServiceAPI serviceAPI = new ServiceApiImpl();

        serviceAPI.displaySpectacles();
        serviceAPI.selectSpectacle(1);
        serviceAPI.showSeatsForSpectacle();

        serviceAPI.addSpectacle();

        serviceAPI.displaySpectacles();
        serviceAPI.selectSpectacle(2);
        serviceAPI.showSeatsForSpectacle();

    }
}
