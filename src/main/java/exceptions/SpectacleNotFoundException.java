package exceptions;

public class SpectacleNotFoundException extends RuntimeException{

    public SpectacleNotFoundException() {
        super("Spectacle not found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
