package se.kth.iv1351.sem4.integration;

public class RentalDBException extends Exception {

    /**
    * an exception for the database
    * 
    * @param message the message
    */
    public RentalDBException(String message) {
        super(message);
    }

    /**
    * an exception for the database
    * 
    * @param message the message
    * @param cause the cause
    */
    public RentalDBException(String message, Throwable cause) {
        super(message, cause);
    }
}