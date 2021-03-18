package Command;

import Entities.Employee;
import Entities.Passenger;
import Singleton.*;
import javafx.fxml.FXML;
import java.sql.*;

/**
 * Allows the manager to add an employee to the system.
 */
public class AddUser implements Command{

    private final Passenger user;
    private final char[] password;

    public AddUser(Passenger user, char[] password) {
        this.user = user;
        this.password = password;
    }

    @FXML
    public void execute() {
        String sql = "INSERT INTO login(id,password,role,name,email) VALUES(?,?,?,?,?)";
        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getId());
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getEmail());

            pstmt.setString(2, String.valueOf(password));

            if (user instanceof Employee) {
                pstmt.setString(3, ((Employee) user).getRole());
                EmployeeAccess.getInstance().add((Employee) user);
            }
            else {
                pstmt.setString(3, "Passenger");
                PassengerAccess.getInstance().add(user);
            }
            pstmt.executeUpdate();

            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
