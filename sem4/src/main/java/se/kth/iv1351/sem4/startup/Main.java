package se.kth.iv1351.sem4.startup;
import se.kth.iv1351.sem4.model.*;
import se.kth.iv1351.sem4.integration.*;
import java.util.ArrayList;
public class Main {


    public static void main(String[] args) throws Exception{

        RentalDAO renrenterenren = new RentalDAO();
        ArrayList<InstrumentDTO> rents = renrenterenren.getAllInstruments();
        ArrayList<RentalDTO> instrus = renrenterenren.getAllRentals();
        RentalInfo rentalHandler = new RentalInfo();
       

        
        for(InstrumentDTO instrumentsas: rentalHandler.checkAvailibleInstrument(instrus, rents)) {
            System.out.println(instrumentsas.toString());
        }
        
    }
}
