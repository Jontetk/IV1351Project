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
import se.kth.iv1351.sem4.model.StudentsRentingDTO;




public class RentalDAO {
    private Connection connection;
    private PreparedStatement findAllInstruments;
    private PreparedStatement findAllRentals;
    private PreparedStatement findStudentsByPNR;
    private PreparedStatement findAllStudentsRenting;
    
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
        findAllRentals = connection.prepareStatement("SELECT "+RENT_ID+", "+S_DATE+", "+E_DATE+", "+INSTRUMENT_ID+ " "
        +"FROM seminar.rental");
        findStudentsByPNR = connection.prepareStatement("SELECT "+STUD_ID+", "+PERSON_NR+" "
        +"FROM seminar.student WHERE "+ PERSON_NR +" = ?");
        findAllStudentsRenting = connection.prepareStatement("SELECT "+RENT_ID+", "+STUD_ID+" "
        +"FROM seminar.students_renting");
    }




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
            throw new RentalDBException("Error could not get data. Reason: ", e);
        }
        
        return allInstruments;

    }
    public ArrayList<RentalDTO> getAllRentals() throws RentalDBException{
        ArrayList<RentalDTO> allRentals = new ArrayList<RentalDTO>();
        
        try {
            ResultSet result = findAllRentals.executeQuery();
            {
                while (result.next()) {
                        allRentals.add(
                        new RentalDTO(
                            result.getInt(RENT_ID),
                            result.getDate(S_DATE),
                            result.getDate(E_DATE),
                            result.getInt(INSTRUMENT_ID)
                            ));}
                    connection.commit();
                }
            }
            
          catch (SQLException e) {
            throw new RentalDBException("Error could not get data. Reason: ", e);
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
            throw new RentalDBException("Error could not get data. Reason: ", e);
        }
        
        return student;
    }


    // TODO REMOVE IF UNNCESESSARY 
    public ArrayList<StudentsRentingDTO> getAllStudentsRenting() throws RentalDBException{
        ArrayList<StudentsRentingDTO> allStudentRentals = new ArrayList<StudentsRentingDTO>();
        
        try {
            ResultSet result = findAllStudentsRenting.executeQuery();
            {
                while (result.next()) {
                        allRentals.add(
                        new StudentsRentingDTO(
                            result.getInt(RENT_ID),
                            result.getInt(STUD_ID)
     
                            ));}
                    connection.commit();
                }
            }
            
            catch (SQLException e) {
            throw new RentalDBException("Error could not get data. Reason: ", e);
        }
        
        return allStudentRentals;
        
    }
    

    private void connectToRentalDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sem",
                "postgres", "post");

        connection.setAutoCommit(false);
    }

    

    // TODO CUSTOMIZE CODE HERE PERHAPS
    private void closeResultSet(String failureMsg, ResultSet result) throws RentalDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new RentalDBException(failureMsg + " Could not close result set.", e);
        }
    }


    // TODO CUSTOMIZE CODE HERE PERHAPS
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

