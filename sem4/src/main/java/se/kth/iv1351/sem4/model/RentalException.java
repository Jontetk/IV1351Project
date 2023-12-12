package se.kth.iv1351.sem4.model;


/**
 * Raised when a rental related exeption occurs
 */
public class RentalException extends Exception {

    public RentalException(String message) {
        super(message);
    }

   
    public RentalException(String message, Throwable cause) {
        super(message, cause);
    }
}
