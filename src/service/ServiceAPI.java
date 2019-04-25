package service;

import model.Client;

public interface ServiceAPI {

    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_RESET = "\u001B[0m";

    void displaySpectacles();
    void selectSpectacle(int index);
    void showSeatsForSpectacle();
    void addSpectacle();
    void addClientForSpectacle(Client client);
    void exitApp();
    Client createClient();
    void writeString(String data);
    int numberOfSpectacles();
}
