package model;

public class VipSeat extends Seat {

    private double tax;


    public VipSeat(int number, boolean occupied, int row, double price, double tax) {
        super(number, occupied, row, price+price*tax);
        setType("Armchair");
        this.tax = tax;
    }
}
