package model;

import enums.PaymentType;

import java.util.ArrayList;
import java.util.List;

public class Client implements Comparable{

    private double toPay = 0;
    private int id = 0;
    protected String name;
    protected List<Integer> seats;
    protected PaymentType paymentType = PaymentType.CASH;
    protected String spectacle;

    public Client() {
        seats = new ArrayList<>();
    }

    public Client(String name, List<Integer> seats) {
        this.name = name;
        this.seats = seats;
    }

    public Client(String name, PaymentType paymentType){
        seats = new ArrayList<>();
        this.name = name;
        this.paymentType = paymentType;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Integer> seats) {
        this.seats = seats;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpectacle(String spectacle) {
        this.spectacle = spectacle;
    }

    public String getSpectacle() {
        return spectacle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public int compareTo(Object o) {
        return name.compareTo(((Client) o).getName());
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", seats=" + seats +
                ", paymentType=" + paymentType +
                '}';
    }

    public void setToPay(double value){
        toPay = value;
    }

    public double getToPay(){
        return toPay;
    }
}
