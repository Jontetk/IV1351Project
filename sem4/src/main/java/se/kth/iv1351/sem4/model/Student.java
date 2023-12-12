package se.kth.iv1351.sem4.model;

import java.util.*;


public class Student {

    private int id;
    private String personNumber;
    
    /**
     * An object representing a student
     * @param id of the student 
     * @param personNumber of the student 
     */
    public Student(int id, String personNumber) {
        this.id = id;
        this.personNumber = personNumber;
    }

   
    public int getId() {
        return id;
    }

    public String getPersonNumber() {
        return personNumber;
    }


    /**
     * Checks if the rental is allowed to be terminated.
     * @param rentals a list of all rentals that has been done by this student
     * @param rentalId id of the rental to be terminated
     * @return true if the rental to be terminated both exists in rentals and rental is ongoing (no end date) 
     */
    public boolean rentalCanBeTerminated(ArrayList<RentalDTO> rentals, int rentalId) {
        for(RentalDTO rental : rentals) {
            if(rentalId == rental.getRentalId() && rental.getEndDate() == null)
                return true;
        }
        return false;

    }
    
    /**
     * Creates and rental object representing the rental made by student
     * @param rentals a list of all rentals in the model
     * @param availibleInstruments all instruments availible for rental
     * @param instrumentId id of the instrument to rent
     * @param maxRental the maximum amount of rental that a student can make
     * @return a rental object {@link RentalDTO} representing the rental made by student
     * @throws RentalException
     */
    public RentalDTO rentInstrument(ArrayList<RentalDTO> rentals, ArrayList<InstrumentDTO> availibleInstruments, int instrumentId,int maxRental)
    throws RentalException {
    
        if(! (isRentalAllowed(rentals,maxRental) && isSelectedInstrumentAvalible(availibleInstruments, instrumentId)))
        {
            throw new RentalException("Cannot rent. Instrument not avalible or above limit. "); 
        }
        
        return new RentalDTO(null,new java.sql.Date(new java.util.Date().getTime()), null, instrumentId, this.id);
        
        

    

    }

    /**
     * Checks wheter the rental can be made 
     * @param rentals the list of all rentals in model
     * @param rentalLimit the max allowed ongoing rentals
     * @return true if student's ongoing rental count is below the limit. Returns false otherwise
     */
    private boolean isRentalAllowed(ArrayList<RentalDTO> rentals, int rentalLimit){

        
        int totalRentals = 0;
        for(RentalDTO rental:rentals) {
            if(rental.getStudentId() == this.id && rental.getEndDate() == null)
                totalRentals += 1;
        }
       
        return  rentalLimit > totalRentals;
    }

    /**
     * Checks wheter the instrument with a certain id is avalible to rent
     * @param availibleInstruments a list of all instruments avalible to rent
     * @param instrumentId id of the instrument that is being selected
     * @return true if an avalible instrument with the {@code instrumentId}
     */
    private boolean isSelectedInstrumentAvalible(ArrayList<InstrumentDTO> availibleInstruments, int instrumentId){
        Boolean isAvalible = false;
        for(InstrumentDTO ins: availibleInstruments){
            if(ins.getInstrumentId() == instrumentId){
                isAvalible = true;
                break;
            }
        }
        return isAvalible;
    }

}
