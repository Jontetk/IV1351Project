package se.kth.iv1351.sem4.model;
import java.sql.Date;


public class RentalDTO {
    
    private int rentalId;
    private Date startDate;
    private Date endDate;    
    private int instrumentId;
   



    @Override
    public String toString(){
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Rental: [");
        stringRepresentation.append(" instrumentId: "+this.instrumentId);
        stringRepresentation.append(" brand: "+this.brand);
        stringRepresentation.append(" type: "+this.type);
        stringRepresentation.append(" code: "+this.code);
        stringRepresentation.append(" fee per month: " +this.rentalFee);
        stringRepresentation.append("]");

        return stringRepresentation.toString();
    }
}