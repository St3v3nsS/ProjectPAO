package domain;

import enums.PaymentType;

import java.util.ArrayList;

public class VipClient extends Client {

    public VipClient() {
    }

    public VipClient(String name, ArrayList<Integer> seats) {
        super(name, seats);
        paymentType = PaymentType.CARD;
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }
}
