package se.kth.iv1351.sem4.controller;

import se.kth.iv1351.sem4.integration.RentalDAO;
import se.kth.iv1351.sem4.integration.RentalDBException;

import se.kth.iv1351.sem4.model.*;
import java.util.ArrayList;

public class Controller {
    private final RentalDAO rentaldb;
    private final String dbErrorStr = "Error in database";
    
    public Controller() throws RentalDBException{
        rentaldb = new RentalDAO();

    }


    public ArrayList<InstrumentDTO> getAllAvalibleInstruments() throws RentalException{ 
        try{
        return RentalInfo.checkAvailibleInstrument(rentaldb.getAllRentals(false), rentaldb.getAllInstruments());
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }
    }

  

    public void rentInstrument(String personNumber, int insId) throws RentalException{

        
        try{
        Student currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
        RentalDTO newRental = currentStudent.rentInstrument(rentaldb.getAllRentals(true), this.getAllAvalibleInstruments(), insId, rentaldb.getMaxRentalNumber());

        rentaldb.insertNewRental(newRental);
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }        

        catch(RentalException e) {
            try{
            rentaldb.rollbackOnError(e);
            throw new RentalException(e.getMessage());
            }
            catch(RentalDBException dbe) {
                throw new RentalException(dbErrorStr);
            }
        }

    }
    public ArrayList<RentalDTO> getAllRentalsFromStudent(String personNumber) throws RentalException{
        try {
            Student currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
            return rentaldb.getStudentRentals(currentStudent.getId());
        }
        catch (RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }

    }

    public void terminateRental(String personNumber, int rentalId) throws RentalException{

        try {
            Student currentStudent = rentaldb.getStudentByPeronNumber(personNumber);
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