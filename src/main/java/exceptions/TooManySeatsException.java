package exceptions;

public class TooManySeatsException extends Exception {

    public TooManySeatsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
