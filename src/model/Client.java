package model;

import enums.PaymentType;

import java.util.ArrayList;

public class Client implements Comparable{

    protected String name;
    protected ArrayList<Integer> seats;
    protected PaymentType paymentType;

    public Client() {
        seats = new ArrayList<>();
    }

    public Client(String name, ArrayList<Integer> seats) {
        this.name = name;
        this.seats = seats;
    }

    public Client(String name, PaymentType paymentType){
        seats = new ArrayList<>();
        this.name = name;
        this.paymentType = paymentType;
    }

    public ArrayList<Integer> getSeats() {
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
}
