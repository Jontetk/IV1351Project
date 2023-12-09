package se.kth.iv1351.sem4.model;

import java.util.ArrayList;

import se.kth.iv1351.sem4.integration.RentalDBException;

public class Student {

    private int id;
    private String personNumber;
    
    
    public Student(int id, String personNumber) {
        this.id = id;
        this.personNumber = personNumber;
    }

    


    public void rentInstrument(ArrayList<RentalDTO> rentals,ArrayList<InstrumentDTO> instruments){

        // rental count logic. May need to refactor
        int totalRentals = 0;
        for(RentalDTO rental:rentals) {
            if(rental.getEndDate() == null)
                totalRentals += 1;
        }
        
        // check if total > limit and then throw exception

        


    }


    //cancel rental

}
