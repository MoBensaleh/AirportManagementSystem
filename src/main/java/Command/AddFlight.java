package Command;

/*
  CMPT 270 A5Q5
  @author Blake Stadnyk; 11195866 - BJS645
 */

import java.sql.*;

import AirportManager.AirportManagerController;
import Singleton.AirportAccess;
import Entities.Flight;
import Singleton.FlightsAccess;
import Singleton.dbConnection;
import javafx.fxml.FXML;

/**
 * Allows the user to add a flight to the system.
 */
public class AddFlight implements Command{
    //@Override
    /*public void execute() {
        try {
            Connection c =  dbConnection.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        IOAccess.getInstance().outputString("Adding a new flight...");
        String airline = IOAccess.getInstance().readString("Enter the airline:");
        String number = IOAccess.getInstance().readString("Enter the flight number:");
        String destination = IOAccess.getInstance().readString("Enter the destination:");
        int capacity = IOAccess.getInstance().readInt("Enter the capacity:");

        if (AirportAccess.getInstance().hasFlight(number)) {
            IOAccess.getInstance().outputString("A flight with this number already exists in the system");
        }
        else {
            Flight flight = new Flight(airline, number, destination, null, null);
            AirportAccess.getInstance().assignFlightToGate(flight,AirportAccess.getInstance().availableGates().get(0));
            IOAccess.getInstance().outputString("Flight created.");
        }
    }*/

    private final AirportManagerController airportManagerController;

    public AddFlight(AirportManagerController airportManagerController) {
        this.airportManagerController = airportManagerController;
    }


    @FXML
    public void execute() {
        String sql = "INSERT INTO flights(flightNum,airline,destination,date,time,gate) VALUES(?,?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, airportManagerController.flightnum.getText());
            pstmt.setString(2, airportManagerController.airline.getText());
            pstmt.setString(3, airportManagerController.destination.getText());
            pstmt.setDate(4, Date.valueOf(airportManagerController.date.getValue()));
            pstmt.setTime(5, Time.valueOf(airportManagerController.time.getText()));

            int gate = AirportAccess.getInstance().getGates().firstAvailableStall();
            pstmt.setInt(6,gate);

            pstmt.executeUpdate();
            Flight flight = new Flight(airportManagerController.flightnum.getText(),
                            airportManagerController.airline.getText(),
                            airportManagerController.destination.getText(),
                            Date.valueOf(airportManagerController.date.getValue()),
                            Time.valueOf(airportManagerController.time.getText()),
                            gate);
            FlightsAccess.getInstance().add(flight);
            AirportAccess.getInstance().getGates().assignEntityToStall(flight, gate);

            airportManagerController.clearForm(null);

            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}