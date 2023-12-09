package se.kth.iv1351.sem4.model;
import java.sql.Date;


public class RentalDTO {
    
    private int rentalId;
    private Date startDate;
    private Date endDate;    
    private int instrumentId;
    
    
    public RentalDTO(int rentalId, Date startDate, Date endDate, int instrumentId) {
        this.rentalId = rentalId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.instrumentId = instrumentId;
    }


    public int getRentalId() {
        return rentalId;
    }


    public Date getStartDate() {
        return startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public int getInstrumentId() {
        return instrumentId;
    }
   
    
    @Override
    public String toString(){
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Rental: [");
        stringRepresentation.append(" rentalId: "+this.rentalId);
        stringRepresentation.append(" startDate: "+this.startDate);
        stringRepresentation.append(" endDate: "+this.endDate);
        stringRepresentation.append(" instrumentId: "+this.instrumentId);
        stringRepresentation.append("]");

        return stringRepresentation.toString();
    }


    
}