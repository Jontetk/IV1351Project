package se.kth.iv1351.sem4.model;

import java.util.ArrayList;


public class RentalInfo {



    /**
     * Checks which instruments are avalible to rent
     * @param rentals an {@code ArrayList} of all rentals 
     * @param instruments an {@code ArrayList} all instruments 
     * @return an list of all instruments that are avalible to rent.
     * These instruments are not a part of ongoing rental
     */
    public static ArrayList<InstrumentDTO> checkAvailibleInstrument(ArrayList<RentalDTO> rentals ,ArrayList<InstrumentDTO> instruments) {
        ArrayList<InstrumentDTO> availibleInstruments = new ArrayList<InstrumentDTO>();
        ArrayList<Integer> uvalibleInsturmentIds = new ArrayList<Integer>();
                
        for(RentalDTO rental : rentals){
            if(rental.getEndDate() == null){
                uvalibleInsturmentIds.add(rental.getInstrumentId());
            }
        }
        
        for(InstrumentDTO instrument: instruments) {
            if(!uvalibleInsturmentIds.contains(instrument.getInstrumentId()))
                availibleInstruments.add(instrument);
        }


        return availibleInstruments;
    }

   

}
