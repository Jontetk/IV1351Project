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
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new RentalDBException("Could not connect to datasource.", exception);
        }
    }

    private void prepareStatements() throws SQLException {
        findAvalibleInstruments = connection.prepareStatement("SELECT i."+INSTRUMENT_ID+", i."+BRAND+", i."+TYPE+", i."+CODE+", i."+RENT_FEE+" "
        +"FROM seminar.instrument AS i "
        +"LEFT JOIN seminar.rental AS r ON i."+INSTRUMENT_ID+" = r."+INSTRUMENT_ID+" AND r.end_date IS NULL "
        +"WHERE r."+INSTRUMENT_ID+" IS NULL OR (r."+INSTRUMENT_ID+" IS NOT NULL AND r.end_date IS NOT NULL);");
        

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



}

