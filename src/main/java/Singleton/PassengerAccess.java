package Singleton;

import Entities.Passenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;


/**
 * A Passenger Map using the singleton pattern
 */
public class PassengerAccess {

    /**
     * The singleton passenger map
     */
    private static ObservableList<Passenger> passengers;

    /**
     * Private do nothing constructor
     */
    private PassengerAccess() {
    }

    /**
     * Gets the instance of the map
     *
     * @return  the map instance
     */
    public static ObservableList<Passenger> getInstance() {
        if (passengers == null) initialize();
        return passengers;
    }

    /**
     * Initialize the passenger map with the data in the database
     */
    private static void initialize() {
        if (passengers == null) {
            passengers = FXCollections.observableArrayList();
            try {
                String sql = "SELECT name, id, email, checkin, parkingStall FROM login WHERE role ='Passenger'";
                Connection conn = dbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    passengers.add(new Passenger(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getInt(5)));
                }

                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}