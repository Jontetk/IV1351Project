package se.kth.iv1351.sem4.model;

import se.kth.iv1351.sem4.integration.RentalDBException;

import java.sql.*;
import java.util.*;

public class Student {

    private int id;
    private String personNumber;
    
    
    public Student(int id, String personNumber) {
        this.id = id;
        this.personNumber = personNumber;
    }

    


    public void rentInstrument(ArrayList<RentalDTO> rentals, ArrayList<InstrumentDTO> availibleInstruments, int instrumentId,int maxRental)
    throws Exception {
    
        if(! (isRentalAllowed(rentals,maxRental) && isSelectedInstrumentAvalible(availibleInstruments, instrumentId)))
        {
            throw new Exception("Cannot rent. Instrument not avalible or above limit. "); 
        }
        
        rentals.add(new RentalDTO(null,new java.sql.Date(new java.util.Date().getTime()), null, instrumentId, this.id));
        
        

    

    }

    
    private boolean isRentalAllowed(ArrayList<RentalDTO> rentals, int rentalLimit){

        // rental count logic. May need to refactor into if rental valid function
        int totalRentals = 0;
        for(RentalDTO rental:rentals) {
            if(rental.getStudentId() == this.id)
                totalRentals += 1;
        }
        // check if total > limit and then returns bool
        return totalRentals > rentalLimit;
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
