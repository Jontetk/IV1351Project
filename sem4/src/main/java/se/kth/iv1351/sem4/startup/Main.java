package se.kth.iv1351.sem4.startup;
import se.kth.iv1351.sem4.controller.*;
import se.kth.iv1351.sem4.view.BlockingInterpreter;
public class Main {



    /***
     * Initializes entire program
     */
    public static void main(String[] args) throws Exception{
        
        Controller cont = new Controller();
        BlockingInterpreter block = new BlockingInterpreter(cont);
        block.handleCmds();
        
    }
}
