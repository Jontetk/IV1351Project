package se.kth.iv1351.sem4.model;

public class InstrumentDTO {
    private int instrumentId;
    private String brand;
    private String type;
    private String code;
    private float rentalFee;
   
    /**
     * an object representing an instrument 
     * @param instrumentId 
     * @param brand
     * @param type the type of the instrument. For example: guitar, piano, violin etc...
     * @param code unique code given to the instrument
     * @param rentalFee fee (paid per month) to rent the instrument
     */
    public InstrumentDTO(int instrumentId, String brand, String type, String code, float rentalFee) {
        this.instrumentId = instrumentId;
        this.brand = brand;
        this.type = type;
        this.code = code;
        this.rentalFee = rentalFee;
    }

    public int getInstrumentId() {
        return instrumentId;
    }
    public String getBrand() {
        return brand;
    }
    public String getType() {
        return type;
    }
    public String getCode() {
        return code;
    }
    public float getRentalFee() {
        return rentalFee;
    }

    
    
    @Override
    public String toString(){
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Instrument: [");
        stringRepresentation.append(" instrumentId: "+this.instrumentId);
        stringRepresentation.append(" brand: "+this.brand);
        stringRepresentation.append(" type: "+this.type);
        stringRepresentation.append(" code: "+this.code);
        stringRepresentation.append(" fee per month: " +this.rentalFee);
        stringRepresentation.append("]");

        return stringRepresentation.toString();
    }
}
