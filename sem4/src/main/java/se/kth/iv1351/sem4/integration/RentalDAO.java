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
    private PreparedStatement findStudentRentals;
    private PreparedStatement findAllInstrumentsLocking;

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
    





    /**
     * The rentalDAO for handling database connection
     * @throws RentalDBException If the database could not connect
     */
    public RentalDAO() throws RentalDBException {
        try {
            connectToRentalDB();
            prepareStatements();
        } catch ( SQLException exception) {
            throw new RentalDBException("Could not connect to datasource.", exception);
        }
    }

    /**
     * Statements that are used
     * @throws SQLException If there was an error with getting the data
     */
    private void prepareStatements() throws SQLException {
        findAllInstruments = connection.prepareStatement("SELECT "+INSTRUMENT_ID+", "+BRAND+", "+TYPE+", "+CODE+", "+RENT_FEE+" "
        +"FROM seminar.instrument;");

        findAllInstrumentsLocking = connection.prepareStatement("SELECT "+INSTRUMENT_ID+", "+BRAND+", "+TYPE+", "+CODE+", "+RENT_FEE+" "
        +"FROM seminar.instrument FOR UPDATE;");


        findAllRentals = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+", "+STUD_ID+ " "
        +"FROM seminar.rental;");
        lockRentalTable = connection.prepareStatement("LOCK TABLE seminar.rental in SHARE ROW EXCLUSIVE MODE;");
        findStudentsByPNR = connection.prepareStatement("SELECT "+STUD_ID+", "+PERSON_NR+" "
        +"FROM seminar.student WHERE "+ PERSON_NR +" = ?;" );
        findMaxRental = connection.prepareStatement("SELECT "+MAX_RENTAL+" "
        +"from seminar.max_rental_per_student ORDER BY set_date DESC LIMIT 1;");
        insertRental =connection.prepareStatement("INSERT into seminar.rental ("+S_DATE+","+E_DATE+","+INSTRUMENT_ID+","+STUD_ID+") VALUES (?,?,?,?);");
        findStudentRentalsLocking = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+", "+STUD_ID+" FROM seminar.rental WHERE "+STUD_ID+" = ? FOR NO KEY UPDATE;");
        findStudentRentals = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+", "+STUD_ID+" FROM seminar.rental WHERE "+STUD_ID+" = ?;");
        setRentalEndDate = connection.prepareStatement("UPDATE seminar.rental SET "+E_DATE+" = ? WHERE "+RENT_ID+" = ?;");
        

    }

    /**
     * Removes a selected rental by setting its end date to the current date
     * 
     * @param rentalId The id of the rental to be removed
     * @throws RentalDBException If there was a problem in the database
     */
    public void deleteRental(int rentalId) throws RentalDBException{
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
     * Gets all rentals for a specific student
     * 
     * @param id The id of the {@link se.kth.iv1351.sem4.model.Student Student} that has the rentals
     * @param lock If the row should get locked
     * @return An {@link java.util.ArrayList ArrayList} containing all rentals of the student
     * @throws RentalDBException If there was a problem with getting the data
     */
    public ArrayList<RentalDTO> readStudentRentals(int id,boolean lock)  throws RentalDBException{
        ArrayList<RentalDTO> studentRentals = new ArrayList<>();
        try {
            PreparedStatement statement;
            if(lock) 
                statement = findStudentRentalsLocking;
            else
                statement = findStudentRentals;

            statement.setInt(1, id);
            ResultSet result = statement .executeQuery();
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

    
    /**
     * Gets all instruments
     * @return An {@link java.util.ArrayList ArrayList} containing all instruments
     * @throws RentalDBException If there was a problem with getting the data
     */
    public ArrayList<InstrumentDTO> readAllInstruments(boolean lock) throws RentalDBException{
        ArrayList<InstrumentDTO> allInstruments = new ArrayList<InstrumentDTO>();
        PreparedStatement statement;
        if(lock)
            statement = findAllInstrumentsLocking;
        else
            statement = findAllInstruments;
             
        try {
            ResultSet result = statement.executeQuery();
            
                while (result.next()) {
                        allInstruments.add(
                        new InstrumentDTO(
                            result.getInt(INSTRUMENT_ID),
                            result.getString(BRAND),
                            result.getString(TYPE),
                            result.getString(CODE),
                            result.getFloat(RENT_FEE)
                            ));}
                    if(!lock)
                        connection.commit();
                }
            
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return allInstruments;

    }


    /**
     * Gets all the rentals
     * 
     * @param lock If the table should get locked
     * @return An {@link java.util.ArrayList ArrayList} containing all rentals
     * @throws RentalDBException If there was a problem with getting the data
     */
    public ArrayList<RentalDTO> readAllRentals(boolean lock) throws RentalDBException{
        ArrayList<RentalDTO> allRentals = new ArrayList<RentalDTO>();
            PreparedStatement statement = findAllRentals;
          
         
        try {
            if(lock){
                lockRentalTable.execute();
            }

             ResultSet result = statement.executeQuery();
            
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
            
        catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        
        return allRentals;
        
    }

    /**
     * Gets the student that has a specified person number
     * 
     * @param personNumber the specified person number
     * @return A {@link se.kth.iv1351.sem4.model.Student Student} that has the person number 
     * @throws RentalDBException If there was a problem with getting the data
     */
    public Student readStudentByPeronNumber(String personNumber) throws RentalDBException{
        Student student = null;
        try {
            findStudentsByPNR.setString(1,personNumber);
            ResultSet result = findStudentsByPNR.executeQuery();
            
                if(result.next()){
                    student = new Student(
                    result.getInt(STUD_ID),
                    result.getString(PERSON_NR));
                }
                connection.commit();
            
            }
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return student;
    }


    /**
     * Returns the max number of rentals that a student can have
     * 
     * @return the max amount of rentals
     * @throws RentalDBException If there was a problem with getting the data
     */

    public Integer readMaxRentalNumber() throws RentalDBException{

        Integer maxRental = null;
        try {
            ResultSet result = findMaxRental.executeQuery();
            
                if(result.next()){
                    maxRental = result.getInt(MAX_RENTAL);
                }
                connection.commit();
            
            }
            
          catch (SQLException e) {
            handleException("Error could not get data. Reason: ", e);
        }
        
        return maxRental;

    }

    /**
     * Inserts a new rental into the database
     * 
     * @param rental The {@link se.kth.iv1351.sem4.model.RentalDTO RentalDTO} to be inserted into the database
     * @throws RentalDBException If there was a problem with getting the data
     */
    public void createNewRental(RentalDTO rental) throws RentalDBException{
     
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


    
    /** Connects to the database
     * 
     * @throws SQLException if there was an error connecting
     */
    private void connectToRentalDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sem",
                "postgres", "post");

        connection.setAutoCommit(false);
        
    }

    
    /**
     * Calls handle exception with the error
     * 
     * @param error the error
     * @throws RentalDBException The exception thrown by handle exception
     */
    public void rollbackOnError(Exception error) throws RentalDBException {
            handleException("Transaction failed",error);
        
    }

    /**
     * Handles any exception by rolling back the database to before the current transaction
     * 
     * @param failureMsg The failure message 
     * @param cause The cause of the exception
     * @throws RentalDBException A rentalDBException created from the message and the cause
     */
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

