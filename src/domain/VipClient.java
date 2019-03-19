package domain;

import enums.PaymentType;

import java.util.ArrayList;

public class VipClient extends Client {

    public VipClient() {
    }

    public VipClient(String name) {
        super(name);
        seats = new ArrayList<VipSeat>();
        paymentType = PaymentType.CARD;
    }

}
