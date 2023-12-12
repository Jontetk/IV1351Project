package se.kth.iv1351.sem4.model;



import java.sql.*;
import java.util.*;

public class Student {

    private int id;
    private String personNumber;
    
    
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


    public boolean rentalCanBeTerminated(ArrayList<RentalDTO> rentals, int rentalId) {
        for(RentalDTO rental : rentals) {
            if(rentalId == rental.getRentalId() && rental.getEndDate() == null)
                return true;
        }
        return false;

    }
    
    public RentalDTO rentInstrument(ArrayList<RentalDTO> rentals, ArrayList<InstrumentDTO> availibleInstruments, int instrumentId,int maxRental)
    throws RentalException {
    
        if(! (isRentalAllowed(rentals,maxRental) && isSelectedInstrumentAvalible(availibleInstruments, instrumentId)))
        {
            throw new RentalException("Cannot rent. Instrument not avalible or above limit. "); 
        }
        
        return new RentalDTO(null,new java.sql.Date(new java.util.Date().getTime()), null, instrumentId, this.id);
        
        

    

    }

    
    private boolean isRentalAllowed(ArrayList<RentalDTO> rentals, int rentalLimit){

        // rental count logic. May need to refactor into if rental valid function
        int totalRentals = 0;
        for(RentalDTO rental:rentals) {
            if(rental.getStudentId() == this.id && rental.getEndDate() == null)
                totalRentals += 1;
        }
        // check if limit > total and then returns bool
        return  rentalLimit > totalRentals;
    }

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

    //cancel rental

}
