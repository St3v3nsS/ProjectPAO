package domain;

import enums.PaymentType;

import java.util.ArrayList;

public class Client {

    protected String name;
    protected ArrayList<? extends Seat> seats;
    protected PaymentType paymentType;

    public Client() {
        seats = new ArrayList<>();
    }

    public Client(String name) {
        this.name = name;
    }

    public Client(String name, PaymentType paymentType){
        seats = new ArrayList<>();
        this.name = name;
        this.paymentType = paymentType;
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
}
