package se.kth.iv1351.sem4.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.kth.iv1351.sem4.integration.RentalDBException;


// TODO add commit,handleEzxception and closeResultSet functions

public class RentalDAO {
    private Connection connection;
    
    public RentalDAO() throws RentalDBException {
        try {
            connectToRentalDB();
            // TODO add prepareStatements initializaiton 
        } catch (ClassNotFoundException | SQLException exception) {
            throw new RentalDBException("Could not connect to datasource.", exception);
        }
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
            System.out.println("Failed to commit"+e.getMessage()); //TODO add HANDLE EXCEPTION FUNCITON CALL HERE see git 
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

