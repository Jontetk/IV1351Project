package se.kth.iv1351.sem4.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import se.kth.iv1351.sem4.integration.RentalDBException;
import se.kth.iv1351.sem4.model.InstrumentDTO;




public class RentalDAO {
    private Connection connection;
    private PreparedStatement findAvalibleInstruments;
    public static final String INSTRUMENT_ID = "instrument_id";
    public static final String BRAND = "brand";
    public static final String TYPE = "instrument_type";
    public static final String CODE = "instrument_code";
    public static final String RENT_FEE = "rental_fee";
    
    public RentalDAO() throws RentalDBException {
        try {
            connectToRentalDB();
            // TODO add prepareStatements initializaiton 
        } catch (ClassNotFoundException | SQLException exception) {
            throw new RentalDBException("Could not connect to datasource.", exception);
        }
    }

    private void prepareStatements() throws SQLException {
        findAvalibleInstruments = connection.prepareStatement("CREATE OR REPLACE TEMPORARY VIEW rented AS "
        +"SELECT i."+INSTRUMENT_ID+" FROM instrument AS i "
        +"JOIN rental AS r  ON r."+INSTRUMENT_ID+" = i."+INSTRUMENT_ID+" WHERE end_date IS NULL; "
        +"SELECT i."+INSTRUMENT_ID+","+BRAND+","+TYPE+","+CODE+",rental_fee FROM instrument AS i "
        +"FULL JOIN rented AS r "
        +"ON i."+INSTRUMENT_ID+" = r."+INSTRUMENT_ID+" "
        +"WHERE r."+INSTRUMENT_ID+" IS NULL;");
    }

    public ArrayList<InstrumentDTO> findAvaInstruments() throws RentalDBException{
        ArrayList<InstrumentDTO> allInstruments = new ArrayList<>();
        
        try {
            ResultSet result = findAvalibleInstruments.executeQuery();
            {
                while (result.next()) {
                        allInstruments.add(
                        new InstrumentDTO(
                            result.getInt(INSTRUMENT_ID),
                            result.getString(BRAND),
                            result.getString(TYPE),
                            result.getString(CODE),
                            result.getFloat(RENT_FEE)
                            ));
            }
                    
                }
            }
          catch (SQLException e) {
            throw new RentalDBException("Error could not get data. Reason: ", e);
        }
        
        return allInstruments;

    }

    

    private void connectToRentalDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sem",
                "postgres", "post");

        connection.setAutoCommit(false);
    }

    public void commit() throws RentalDBException {
        try {
            connection.commit();
        } catch (SQLException e) {
            handleException("Failed to commit",e); 
            
        }
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


    public static void main(String[] args) {
        try {
               new RentalDAO();
        } catch (Exception e) {
           e.printStackTrace();
        }
     
    }
}

