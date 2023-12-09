package se.kth.iv1351.sem4.controller;

import se.kth.iv1351.sem4.integration.RentalDAO;
import se.kth.iv1351.sem4.integration.RentalDBException;

public class Controller {
    private final RentalDAO rentaldb;

    
    public Controller() throws RentalDBException{
        rentaldb = new RentalDAO();
    }

    
}