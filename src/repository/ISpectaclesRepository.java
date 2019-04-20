package repository;

import model.Spectacle;

import java.util.List;

public interface ISpectaclesRepository {
    Spectacle findByName(String name);
    Spectacle findByIndex(int index);
    void addSpectacle(Spectacle spectacle);
    List<Spectacle> findAll();
}
