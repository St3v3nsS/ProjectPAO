package domain;

public class Seat {

    protected int number;
    protected boolean occupied;
    protected int row;
    protected double price;
    protected String type = "Normal";

    public Seat() {
    }

    public Seat(int number, boolean occupied, int row, double price) {
        this.number = number;
        this.occupied = occupied;
        this.row = row;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
