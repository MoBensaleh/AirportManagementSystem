package Command;

import AirportManager.AirportManagerController;
import Entities.Flight;
import Singleton.AirportAccess;
import Singleton.FlightsAccess;
import Singleton.DBConnection;
import javafx.fxml.FXML;

import java.sql.*;

/**
 * Allows the manager to add a flight to the system.
 */
public class AddFlight implements Command{
    private final AirportManagerController airportManagerController;

    public AddFlight(AirportManagerController airportManagerController) {
        this.airportManagerController = airportManagerController;
    }


    @FXML
    public void execute() {
        String sql = "INSERT INTO flights(flightNum,airline,destination,date,time,gate,capacity) VALUES(?,?,?,?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, airportManagerController.flightnum.getText());
            pstmt.setString(2, airportManagerController.airline.getText());
            pstmt.setString(3, airportManagerController.destination.getText());
            pstmt.setDate(4, Date.valueOf(airportManagerController.date.getValue()));
            pstmt.setTime(5, Time.valueOf(airportManagerController.time.getText()));

            int gate = AirportAccess.getInstance().getGates().firstAvailableStall();
            pstmt.setInt(6,gate);
            pstmt.setInt(7,airportManagerController.capacity.getValue());

            pstmt.executeUpdate();
            Flight flight = new Flight(airportManagerController.flightnum.getText(),
                            airportManagerController.airline.getText(),
                            airportManagerController.destination.getText(),
                            Date.valueOf(airportManagerController.date.getValue()),
                            Time.valueOf(airportManagerController.time.getText()),
                            gate,
                            airportManagerController.capacity.getValue());

            FlightsAccess.getInstance().add(flight);
            if (gate != -1)
            AirportAccess.getInstance().getGates().assignEntityToStall(flight, gate);

            airportManagerController.clearForm(null);

            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
