package se.kth.iv1351.sem4.startup;
import se.kth.iv1351.sem4.model.*;
import se.kth.iv1351.sem4.integration.*;
import java.util.ArrayList;
public class Main {


    public static void main(String[] args) throws Exception{

        RentalDAO renrenterenren = new RentalDAO();
        ArrayList<InstrumentDTO> arararar = renrenterenren.findAvaInstruments();
        for(InstrumentDTO instrumentsas:arararar) {
            System.out.println(instrumentsas.toString());
        }
        
    }
}
