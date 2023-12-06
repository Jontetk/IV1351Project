package se.kth.iv1351.sem4.integration;

public class RentalDBException extends Exception {

   
    public RentalDBException(String message) {
        super(message);
    }

   
    public RentalDBException(String message, Throwable cause) {
        super(message, cause);
    }
}