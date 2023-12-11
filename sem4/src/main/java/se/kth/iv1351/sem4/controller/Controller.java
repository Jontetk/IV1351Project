package se.kth.iv1351.sem4.controller;

import se.kth.iv1351.sem4.integration.RentalDAO;
import se.kth.iv1351.sem4.integration.RentalDBException;

import se.kth.iv1351.sem4.model.*;
import java.util.ArrayList;

public class Controller {
    private final RentalDAO rentaldb;

    
    public Controller() throws RentalDBException{
        rentaldb = new RentalDAO();

    }


    public ArrayList<InstrumentDTO> getAllAvalibleInstruments() throws RentalDBException{ 
        return RentalInfo.checkAvailibleInstrument(rentaldb.getAllRentals(false), rentaldb.getAllInstruments());
    }

  

    public void rentInstrument(String personNumber, int insId) throws RentalDBException, Exception{

        Student currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
        try{
        RentalDTO newRental = currentStudent.rentInstrument(rentaldb.getAllRentals(true), this.getAllAvalibleInstruments(), insId, rentaldb.getMaxRentalNumber());

        rentaldb.insertNewRental(newRental);
        }
        catch(Exception e) {
            rentaldb.rollbackOnError(e);
        }

    }



}