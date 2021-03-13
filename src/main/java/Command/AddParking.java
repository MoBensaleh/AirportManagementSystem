package Command;

import java.sql.*;

import Passenger.ParkingController;
import Entities.Passenger;
import Singleton.AirportAccess;
import Singleton.PassengerMapAccess;
import Singleton.dbConnection;
import javafx.fxml.FXML;

/**
 * Allows a passenger to request parking
 */
public class AddParking implements Command{
    private final ParkingController parkingController;

    public AddParking(ParkingController parkingController) {
            this.parkingController = parkingController;
            }


    @FXML
    public void execute() {
        String sql = "INSERT INTO parking(name,id,email,checkin,parkingStall) VALUES(?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, parkingController.nameField.getText());
            pstmt.setString(2, parkingController.idField.getText());
            pstmt.setString(3, parkingController.emailField.getText());
            pstmt.setDate(4, Date.valueOf(parkingController.CheckinDatePicker.getValue()));

            int parkingStall = AirportAccess.getInstance().getParkingStalls().firstAvailableStall();
            pstmt.setInt(5, parkingStall);

            pstmt.executeUpdate();

            Passenger passenger = PassengerMapAccess.getInstance().get(parkingController.idField.getText());
            if (passenger == null) {
                passenger = new Passenger(parkingController.nameField.getText(),
                        parkingController.idField.getText(),
                        parkingController.emailField.getText(),
                        Date.valueOf(parkingController.CheckinDatePicker.getValue()),
                        parkingStall);
                PassengerMapAccess.getInstance().put(passenger.getNumber(),passenger);
            }

            AirportAccess.getInstance().getParkingStalls().assignEntityToStall(passenger, parkingStall);
            passenger.setParkingStallLabel(parkingStall);

            parkingController.clearReserveForm(null);

            pstmt.close();




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
