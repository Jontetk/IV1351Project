package se.kth.iv1351.sem4.model;

import java.util.ArrayList;

public class RentalInfo {




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
