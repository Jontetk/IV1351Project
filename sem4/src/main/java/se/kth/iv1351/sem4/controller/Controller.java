package se.kth.iv1351.sem4.controller;

import se.kth.iv1351.sem4.integration.RentalDAO;
import se.kth.iv1351.sem4.integration.RentalDBException;

import se.kth.iv1351.sem4.model.*;
import java.util.ArrayList;

public class Controller {
    private final RentalDAO rentaldb;
    private final String dbErrorStr = "Error in database";
    private Student currentStudent = null;
    
    public Controller() throws RentalDBException{
        rentaldb = new RentalDAO();

    }

    public void unSelectStudent() {
        this.currentStudent = null;
    }
    

    public void selectStudent(String personNumber) throws RentalException{
        try{
        this.currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
        }
        catch(RentalDBException e){
            throw new RentalException(dbErrorStr);
        }

    }

    public ArrayList<InstrumentDTO> getAllAvalibleInstruments() throws RentalException{ 
        try{
        return RentalInfo.checkAvailibleInstrument(rentaldb.getAllRentals(false), rentaldb.getAllInstruments());
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }
    }

  


    public void rentInstrument(int insId) throws RentalException, RentalDBException{

        if(currentStudent == null) {
            throw new RentalException("No student set");
        }
        try{
        
        RentalDTO newRental = currentStudent.rentInstrument(rentaldb.getAllRentals(true), this.getAllAvalibleInstruments(), insId, rentaldb.getMaxRentalNumber());

        rentaldb.insertNewRental(newRental);
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }        

        catch(RentalException err) {
            try{
                rentaldb.rollbackOnError(err);
            }
            catch(RentalDBException dbe){
                throw new RentalException(err.getMessage());
            }
        }
        

    }
    public ArrayList<RentalDTO> getAllRentalsFromStudent() throws RentalException{
        if(currentStudent == null) {
            throw new RentalException("No student set");
        }
        try {
            
            return rentaldb.getStudentRentals(currentStudent.getId());
        }
        catch (RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }

    }

    public void terminateRental(int rentalId) throws RentalException{
        if(currentStudent == null) {
            throw new RentalException("No student set");
        }

        try {
           
            ArrayList<RentalDTO> studRentals = rentaldb.getStudentRentals(currentStudent.getId());
        

            if (currentStudent.rentalCanBeTerminated(studRentals,rentalId))
                rentaldb.removeRental(rentalId);
            else {
                throw new RentalException("Student does not have that rental");
            }
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }
        

    }




}