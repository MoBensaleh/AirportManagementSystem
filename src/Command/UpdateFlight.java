package Command;

import AirportManager.AirportManagerController;
import Entities.Flight;
import dbUtil.dbConnection;
import javafx.collections.ObservableList;

import java.sql.*;

public class UpdateFlight implements Command {

    private static ObservableList<Flight> selectedFlights;

    private AirportManagerController airportManagerController;

    public UpdateFlight(ObservableList<Flight> selectedFlights, AirportManagerController airportManagerController) {

        this.selectedFlights = selectedFlights;
        this.airportManagerController = airportManagerController;
    }

    @Override
    public void execute() throws SQLException {

        String sql = "UPDATE flights SET airline = ?, destination = ?, date = ?, time = ? WHERE flightNum = ?";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (Flight flight1 : selectedFlights) {

                pstmt.setString(5, flight1.getFlightNumber());

                pstmt.setString(1, airportManagerController.airline.getText());
                pstmt.setString(2, airportManagerController.destination.getText());
                pstmt.setDate(3, Date.valueOf(airportManagerController.date.getValue()));
                pstmt.setTime(4, Time.valueOf(airportManagerController.time.getText()));

                pstmt.executeUpdate();

            }

            conn.close();
            pstmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        this.airportManagerController.clearForm(null);
        this.airportManagerController.loadFLightData();
    }
}
