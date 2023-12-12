package se.kth.iv1351.sem4.model;
import java.sql.Date;


public class RentalDTO {
    
    private Integer rentalId;
    private Date startDate;
    private Date endDate;    
    private int instrumentId;
    private int studentId;
    
    /**
     * An object representing a rental object
     * @param rentalId id of the rental
     * @param startDate date of when rental starts.
     * @param endDate End date of the rental. Set when the rental is terminated. This field is Null on ongoing rentals.  
     * @param instrumentId id of the instrument being rented
     * @param studentId id of the student that is participating in the rental
     */
    public RentalDTO(Integer rentalId, Date startDate, Date endDate, int instrumentId, int studentId) {
        this.rentalId = rentalId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.instrumentId = instrumentId;
        this.studentId = studentId;
        
    }

    public Integer getRentalId() {
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
    public int getStudentId() {
        return studentId;
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