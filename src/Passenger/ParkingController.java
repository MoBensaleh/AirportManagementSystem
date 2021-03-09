package Passenger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class ParkingController {

    @FXML
    private Button closeButton;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameError;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailError;

    @FXML
    private DatePicker CheckInDatePicker;

    @FXML
    private DatePicker CheckOutDatePicker;


    public void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }


    public void handleNameField(Event event) {
        if ((nameField.getText()).matches("[A-Za-z\\s]{2,}")) {
            nameError.setText("valid");
            nameError.setTextFill(Color.GREEN);
        } else {
            nameError.setText("Name must contain only letters");
            nameError.setTextFill(Color.RED);
        }
    }

    public void handleEmailField(Event event) {

        String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if ((emailField.getText()).matches(EMAIL_REGEX)) {
            emailError.setText("valid");
            emailError.setTextFill(Color.GREEN);
        } else {
            emailError.setText("Must be in this form : user@domain.com");
            emailError.setTextFill(Color.RED);
        }
    }
}