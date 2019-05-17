package repository;

import model.Seat;

import java.util.List;

public interface ISeatRepository {

    void updateOccupied(Integer seat, int idSpec);
    void unsetOccupied(Integer seat, int idSpec);
    List<Seat> getSeatsForSpectacle(int idSpec);
    void addSeat(Seat seat, int idSpec);

}
