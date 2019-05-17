package exceptions;

public class OccupiedSeatException extends RuntimeException {
    public OccupiedSeatException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
