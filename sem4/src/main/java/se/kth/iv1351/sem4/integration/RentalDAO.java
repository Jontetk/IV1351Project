package se.kth.iv1351.sem4.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;



import se.kth.iv1351.sem4.integration.RentalDBException;
import se.kth.iv1351.sem4.model.InstrumentDTO;
import se.kth.iv1351.sem4.model.RentalDTO;
import se.kth.iv1351.sem4.model.Student;





public class RentalDAO {
    private Connection connection;
    private PreparedStatement findAllInstruments;
    private PreparedStatement findAllRentals;
    private PreparedStatement findStudentsByPNR;
    private PreparedStatement findMaxRental;
    private PreparedStatement lockRentalTable;
    private PreparedStatement insertRental;
    private PreparedStatement findStudentRentalsLocking;
    private PreparedStatement setRentalEndDate;
    

    public static final String INSTRUMENT_ID = "instrument_id";
    public static final String BRAND = "brand";
    public static final String TYPE = "instrument_type";
    public static final String CODE = "instrument_code";
    public static final String RENT_FEE = "rental_fee";
    public static final String RENT_ID = "rental_id";
    public static final String S_DATE = "start_date";
    public static final String E_DATE = "end_date";
    public static final String STUD_ID = "student_id";
    public static final String PERSON_NR = "person_number";
    public static final String MAX_RENTAL = "max_rental";






    
    public RentalDAO() throws RentalDBException {
        try {
            connectToRentalDB();
            prepareStatements();
        } catch ( SQLException exception) {
            throw new RentalDBException("Could not connect to datasource.", exception);
        }
    }

    private void prepareStatements() throws SQLException {
        findAllInstruments = connection.prepareStatement("SELECT "+INSTRUMENT_ID+", "+BRAND+", "+TYPE+", "+CODE+", "+RENT_FEE+" "
        +"FROM seminar.instrument;");
        findAllRentals = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+", "+STUD_ID+ " "
        +"FROM seminar.rental");
        lockRentalTable = connection.prepareStatement("LOCK TABLE seminar.rental in SHARE ROW EXCLUSIVE MODE;");
        findStudentsByPNR = connection.prepareStatement("SELECT "+STUD_ID+", "+PERSON_NR+" "
        +"FROM seminar.student WHERE "+ PERSON_NR +" = ?");
        findMaxRental = connection.prepareStatement("SELECT "+MAX_RENTAL+" "
        +"from seminar.max_rental_per_student ORDER BY set_date DESC LIMIT 1;");
        insertRental =connection.prepareStatement("INSERT into seminar.rental ("+S_DATE+","+E_DATE+","+INSTRUMENT_ID+","+STUD_ID+") VALUES (?,?,?,?);");
        findStudentRentalsLocking = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+", "+STUD_ID+" FROM seminar.rental WHERE "+STUD_ID+" = ? FOR NO KEY UPDATE");
        setRentalEndDate = connection.prepareStatement("UPDATE seminar.rental SET "+E_DATE+" = ? WHERE "+RENT_ID+" = ?;");
        

    }

    
    public void removeRental(int rentalId) throws RentalDBException{
        try {

            setRentalEndDate.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            setRentalEndDate.setInt(2, rentalId);
            setRentalEndDate.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            handleException("Error could not set data. Reason: ", e);
        }
    } 


    /**
     * locks rows when run
     */
    public ArrayList<RentalDTO> getStudentRentals(int id)  throws RentalDBException{
        ArrayList<RentalDTO> studentRentals = new ArrayList<>();
        try {
            findStudentRentalsLocking.setInt(1, id);
            ResultSet result = findStudentRentalsLocking .executeQuery();
             while (result.next()) {
                        studentRentals.add(
                        new RentalDTO(
                            result.getInt(RENT_ID),
                            result.getDate(S_DATE),
                            result.getDate(E_DATE),
                            result.getInt(INSTRUMENT_ID),
                            result.getInt(STUD_ID)
                            ));
                        }
                    connection.commit();

        } catch (SQLException e) {
           handleException("Error could not get data. Reason: ", e);
        }
       
        return studentRentals;
    }

    
    // do we handle when the list is empty
    public ArrayList<InstrumentDTO> getAllInstruments() throws RentalDBException{
        ArrayList<InstrumentDTO> allInstruments = new ArrayList<InstrumentDTO>();
        
        try {
            ResultSet result = findAllInstruments.executeQuery();
            {
                while (result.next()) {
                        allInstruments.add(
                        new InstrumentDTO(
                            result.getInt(INSTRUMENT_ID),
                            result.getString(BRAND),
                            result.getString(TYPE),
                            result.getString(CODE),
                            result.getFloat(RENT_FEE)
                            ));}
                    connection.commit();
                }
            }
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return allInstruments;

    }


    // TODO LOCK this table when renting is happen so no one rets same instrument at same time
    // TIP USE SHARELOCK as it blcoks ROW EXCLUSIVE lock which is needed for insert 
    public ArrayList<RentalDTO> getAllRentals(boolean lock) throws RentalDBException{
        ArrayList<RentalDTO> allRentals = new ArrayList<RentalDTO>();
            PreparedStatement statement = findAllRentals;
          
         
        try {
            if(lock){
                lockRentalTable.execute();
            }

             ResultSet result = statement.executeQuery();
            {
                while (result.next()) {
                        allRentals.add(
                        new RentalDTO(
                            result.getInt(RENT_ID),
                            result.getDate(S_DATE),
                            result.getDate(E_DATE),
                            result.getInt(INSTRUMENT_ID),
                            result.getInt(STUD_ID)
                            ));
                        }
                        if(!lock)
                            connection.commit();
                }
            }
            
        catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        
        return allRentals;
        
    }


    public Student getStudentByPeronNumber(String personNumber) throws RentalDBException{
        Student student = null;
        try {
            findStudentsByPNR.setString(1,personNumber);
            ResultSet result = findStudentsByPNR.executeQuery();
            {
                    if(result.next()){
                        student = new Student(
                        result.getInt(STUD_ID),
                        result.getString(PERSON_NR));
                    }
                    connection.commit();
                }
            }
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return student;
    }
    public Integer getMaxRentalNumber() throws RentalDBException{

        Integer maxRental = null;
        try {
            ResultSet result = findMaxRental.executeQuery();
            {
                    if(result.next()){
                        maxRental = result.getInt(MAX_RENTAL);
                    }
                    connection.commit();
                }
            }
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return maxRental;

    }


    public void insertNewRental(RentalDTO rental) throws RentalDBException{
     
        try {
                insertRental.setDate(1,rental.getStartDate());
                insertRental.setDate(2,rental.getEndDate());
                insertRental.setInt(3,rental.getInstrumentId());
                insertRental.setInt(4,rental.getStudentId());
                insertRental.executeUpdate();
                connection.commit();
                
            }
            
        catch (SQLException e) {
            handleException("Error could not set data. Reason: ", e);
        }
    }


    

    private void connectToRentalDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sem",
                "postgres", "post");

        connection.setAutoCommit(false);
        
    }

    

    public void rollbackOnError(Exception error) throws RentalDBException {
            handleException("Transaction failed",error);
        
    }


    private void handleException(String failureMsg, Exception cause) throws RentalDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new RentalDBException(completeFailureMsg, cause);
        } else {
            throw new RentalDBException(completeFailureMsg);
        }
    }



}

