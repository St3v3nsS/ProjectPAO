package Domain;

public class Place {

    protected int number;
    protected boolean occupied;
    protected String row;
    protected int price;

    public Place() {
    }

    public Place(int number, boolean occupied, String row, int price) {
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

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
