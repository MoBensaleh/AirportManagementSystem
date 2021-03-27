package PopoutControllers;

import Entities.Employee;
import Entities.WorkSchedule;
import Singleton.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyEmployeeInformation implements Initializable {

    @FXML
    public Text employeeIdLabel;
    @FXML
    public Text employeeNameLabel;
    @FXML
    public Text employeeEmailLabel;
    @FXML
    public Text employeeRoleLabel;
    @FXML
    public Text sun;
    @FXML
    public Text mon;
    @FXML
    public Text tues;
    @FXML
    public Text wed;
    @FXML
    public Text thur;
    @FXML
    public Text fri;
    @FXML
    public Text sat;
    @FXML
    public Button done;

    private Employee employee;

    private ObservableList<WorkSchedule> schedule;

    Connection conn = DBConnection.getConnection();



    public ModifyEmployeeInformation() throws SQLException { }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void initialize(Employee employee) {

        this.employee = employee;

        employeeIdLabel.setText(employee.getId());
        employeeNameLabel.setText(employee.getName());
        employeeEmailLabel.setText(employee.getEmail());
        employeeRoleLabel.setText(employee.getRole());

        String sql = "SELECT * FROM workSchedule WHERE employeeId = ?";

        try {
            this.schedule = FXCollections.observableArrayList();

            PreparedStatement pstmt1 = conn.prepareStatement(sql);

            pstmt1.setString(1, this.employee.getId());

            ResultSet rs1 = pstmt1.executeQuery();

            employee.getSchedule().setSunday(rs1.getString(2));
            employee.getSchedule().setMonday(rs1.getString(3));
            employee.getSchedule().setTuesday(rs1.getString(4));
            employee.getSchedule().setWednesday(rs1.getString(5));
            employee.getSchedule().setThursday(rs1.getString(6));
            employee.getSchedule().setFriday(rs1.getString(7));
            employee.getSchedule().setSaturday(rs1.getString(8));

            pstmt1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sun.setText(employee.getSchedule().getSunday());
        mon.setText(employee.getSchedule().getMonday());
        tues.setText(employee.getSchedule().getTuesday());
        wed.setText(employee.getSchedule().getWednesday());
        thur.setText(employee.getSchedule().getThursday());
        fri.setText(employee.getSchedule().getFriday());
        sat.setText(employee.getSchedule().getSaturday());

        done.getScene().getWindow().sizeToScene();
    }

    @FXML
    public void done(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    public static String newDialog(String defaultValue, String fieldName) {

        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle("");
        dialog.setHeaderText("Editing " + fieldName);
        dialog.setContentText("");
        dialog.setGraphic(null);

        Optional<String> answer = dialog.showAndWait();
        return answer.orElse(defaultValue);
    }

    public void dbUpdate(Employee employee) {

        String sql1 = "UPDATE workSchedule SET sunday = ?, monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ? WHERE employeeId = ?";
        String sql2 = "UPDATE login SET name = ?, email = ?, role = ? WHERE id = ?";

        try {
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);

            pstmt1.setString(1, employee.getSchedule().getSunday());
            pstmt1.setString(2, employee.getSchedule().getMonday());
            pstmt1.setString(3, employee.getSchedule().getTuesday());
            pstmt1.setString(4, employee.getSchedule().getWednesday());
            pstmt1.setString(5, employee.getSchedule().getThursday());
            pstmt1.setString(6, employee.getSchedule().getFriday());
            pstmt1.setString(7, employee.getSchedule().getSaturday());
            pstmt1.setString(8, employee.getId());

            pstmt2.setString(1, employee.getName());
            pstmt2.setString(2, employee.getEmail());
            pstmt2.setString(3, employee.getRole());
            pstmt2.setString(4, employee.getId());

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();

            pstmt1.close();
            pstmt2.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void editName(ActionEvent event) {
        employee.setName(newDialog(employeeNameLabel.getText(), "Name"));
        dbUpdate(employee);
        employeeNameLabel.setText(employee.getName());
    }

    public void editEmail(ActionEvent event) {
        employee.setEmail(newDialog(employeeEmailLabel.getText(), "Email"));
        dbUpdate(employee);
        employeeEmailLabel.setText(employee.getEmail());
    }

    public void editRole(ActionEvent event) {
        employee.setRole(newDialog(employeeRoleLabel.getText(), "Role"));
        dbUpdate(employee);
        employeeRoleLabel.setText(employee.getRole());
    }

    public void editSunday(ActionEvent event) {
        employee.getSchedule().setSunday(newDialog(sun.getText(), "Sunday"));
        dbUpdate(employee);
        sun.setText(employee.getSchedule().getSunday());
    }

    public void editMonday(ActionEvent event) {
        employee.getSchedule().setMonday(newDialog(mon.getText(), "Monday"));
        dbUpdate(employee);
        mon.setText(employee.getSchedule().getMonday());
    }

    public void editTuesday(ActionEvent event) {
        employee.getSchedule().setTuesday(newDialog(tues.getText(), "Tuesday"));
        dbUpdate(employee);
        tues.setText(employee.getSchedule().getTuesday());
    }

    public void editWednesday(ActionEvent event) {
        employee.getSchedule().setWednesday(newDialog(wed.getText(), "Wednesday"));
        dbUpdate(employee);
        wed.setText(employee.getSchedule().getWednesday());
    }

    public void editThursday(ActionEvent event) {
        employee.getSchedule().setThursday(newDialog(thur.getText(), "Thursday"));
        dbUpdate(employee);
        thur.setText(employee.getSchedule().getThursday());
    }

    public void editFriday(ActionEvent event) {
        employee.getSchedule().setFriday(newDialog(fri.getText(), "Friday"));
        dbUpdate(employee);
        fri.setText(employee.getSchedule().getFriday());
    }

    public void editSaturday(ActionEvent event) {
        employee.getSchedule().setSaturday(newDialog(sat.getText(), "Saturday"));
        dbUpdate(employee);
        sat.setText(employee.getSchedule().getSaturday());
    }
}