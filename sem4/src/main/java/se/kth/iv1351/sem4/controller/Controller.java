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
        return RentalInfo.checkAvailibleInstrument(rentaldb.getAllRentals(), rentaldb.getAllInstruments());
    }

  

    public void rentInstrument(String personNumber, InstrumentDTO ins) throws RentalDBException{
        Student currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
        currentStudent.rentInstrument
        
    }



}