package Singleton;

import Entities.Passenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;


/**
 * A Passenger Map using the singleton pattern
 */
public class PassengerMapAccess {

    /**
     * The singleton passenger map
     */
    private static ObservableMap<String,Passenger> passengers;

    /**
     * Private do nothing constructor
     */
    private PassengerMapAccess() {
    }

    /**
     * Gets the instance of the map
     *
     * @return  the map instance
     */
    public static ObservableMap<String,Passenger> getInstance() {
        if (passengers == null) initialize();
        return passengers;
    }

    /**
     * Initialize the passenger map with the data in the database
     */
    private static void initialize() {
        if (passengers == null) {
            passengers = FXCollections.observableMap(new TreeMap<>());
            try {
                String sql = "SELECT name, id, email, checkin, parkingStall FROM parking";
                Connection conn = dbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    passengers.put(rs.getString(2),new Passenger(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getInt(5)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}