package se.kth.iv1351.sem4.controller;

import se.kth.iv1351.sem4.integration.RentalDAO;
import se.kth.iv1351.sem4.integration.RentalDBException;

import se.kth.iv1351.sem4.model.*;
import java.util.ArrayList;

public class Controller {
    private final RentalDAO rentaldb;
    private final String dbErrorStr = "Error in database";
    private Student currentStudent = null;
    


    /**
     * Controller for controlling the program
     * 
     * @throws RentalDBException if database could not connect
     */
    public Controller() throws RentalDBException{
        rentaldb = new RentalDAO();

    }

    /**
     * Removes the selected {@link se.kth.iv1351.sem4.model.Student Student}
     * 
     */
    public void unSelectStudent() {
        this.currentStudent = null;
    }
    
    /**
     * Sets the current {@link se.kth.iv1351.sem4.model.Student Student}
     * 
     * @param personNumber The person number of the student to be selected
     * @throws RentalException If there was a problem with the database
     */
    public void selectStudent(String personNumber) throws RentalException{
        try{
            this.currentStudent = rentaldb.readStudentByPeronNumber(personNumber);
        }
        catch(RentalDBException e){
            throw new RentalException(dbErrorStr);
        }

    }
    /**
     * Gets all instruments that are currently not rented
     * 
     * @return An {@link java.util.ArrayList ArrayList} containing all instruments that are availible
     * @throws RentalException If there was an error with the database
     */
    public ArrayList<InstrumentDTO> getAllAvalibleInstruments(boolean lock) throws RentalException{ 
        try{
            return RentalInfo.checkAvailibleInstrument(rentaldb.readAllRentals(false), rentaldb.readAllInstruments(lock));
        }
        catch(RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }
    }

  

    /**
     * Rents an instrument as the currentStudent
     * 
     * @param insId The id of the instrument to rent
     * @throws RentalException If there was error with database or error when renting
     * 
     */
    public void rentInstrument(int insId) throws RentalException{

        if(currentStudent == null) {
            throw new RentalException("No student set");
        }
        try{
        
            RentalDTO newRental = currentStudent.rentInstrument(rentaldb.readAllRentals(true), this.getAllAvalibleInstruments(true), insId, rentaldb.readMaxRentalNumber());
            rentaldb.createNewRental(newRental);
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

    /**
     * gets all the rentals of the currentStudent
     * 
     * @return An {@link java.util.ArrayList ArrayList} containing the rentals of the student
     * @throws RentalException If database error or no student has been set
     */
    public ArrayList<RentalDTO> getAllRentalsFromStudent() throws RentalException{
        if(currentStudent == null) {
            throw new RentalException("No student set");
        }
        try {
            
            return rentaldb.readStudentRentals(currentStudent.getId(),false);
        }
        catch (RentalDBException e) {
            throw new RentalException(dbErrorStr);
        }

    }
    /**
     * Terminates a selected rental from the currentStudent
     * 
     * @param rentalId The id of the rental
     * @throws RentalException If there was a problem with the database or the termination logic
     */
    public void terminateRental(int rentalId) throws RentalException{
        if(currentStudent == null) {
            throw new RentalException("No student set");
        }

        try {
           
            ArrayList<RentalDTO> studRentals = rentaldb.readStudentRentals(currentStudent.getId(),true);
            if (currentStudent.rentalCanBeTerminated(studRentals,rentalId))
                rentaldb.deleteRental(rentalId);
            else {
                throw new RentalException("Student does not have that rental");
            }
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




}