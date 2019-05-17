package repository;

import exceptions.SpectacleNotFoundException;
import model.Spectacle;

import java.util.ArrayList;
import java.util.List;

public class SpectaclesRepository implements ISpectaclesRepository {

    private List<Spectacle> spectacles = new ArrayList<>();

    @Override
    public Spectacle findByName(String name) {
        return spectacles.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(SpectacleNotFoundException::new);
    }

    @Override
    public Spectacle findByIndex(int index) {
        if(index < spectacles.size()){
            return spectacles.get(index);
        }
        throw new SpectacleNotFoundException();
    }

    @Override
    public void addSpectacle(Spectacle spectacle) {
        spectacles.add(spectacle);
    }

    @Override
    public List<Spectacle> findAll() {
        return spectacles;
    }
}
