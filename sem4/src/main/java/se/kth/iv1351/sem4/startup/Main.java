package se.kth.iv1351.sem4.startup;
import se.kth.iv1351.sem4.model.*;
import se.kth.iv1351.sem4.integration.*;
import java.util.ArrayList;
import se.kth.iv1351.sem4.controller.*;
public class Main {


    public static void main(String[] args) throws Exception{
        Controller cont = new Controller();

       ArrayList<RentalDTO> studRents = cont.getAllRentalsFromStudent("13666424-8416");
        for(RentalDTO reee : studRents) {
            System.out.println(reee.toString());
        }
        cont.terminateRental("13666424-8416", 11);

        studRents = cont.getAllRentalsFromStudent("13666424-8416");
        for(RentalDTO reee : studRents) {
            System.out.println(reee.toString());
        }
        
        
    }
}
