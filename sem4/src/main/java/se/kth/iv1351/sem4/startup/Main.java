package se.kth.iv1351.sem4.startup;
import se.kth.iv1351.sem4.model.*;
import se.kth.iv1351.sem4.integration.*;
import java.util.ArrayList;
import se.kth.iv1351.sem4.controller.*;
public class Main {


    public static void main(String[] args) throws Exception{
        Controller cont = new Controller();

       
        cont.rentInstrument("65990487-5909",11);
        ArrayList<InstrumentDTO> instrus = cont.getAllAvalibleInstruments();



        for(InstrumentDTO instrumentsas: instrus) {
            System.out.println(instrumentsas.toString());
        }
        
        
    }
}
