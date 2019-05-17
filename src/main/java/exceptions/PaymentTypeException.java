package exceptions;

public class PaymentTypeException extends Exception {
    public PaymentTypeException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
