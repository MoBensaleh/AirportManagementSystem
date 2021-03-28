package AirportManager;

import Command.*;
import Entities.Employee;
import Entities.Flight;
import Entities.Passenger;
import Entities.User;
import PopoutControllers.AddTasks;
import PopoutControllers.FlightInfo;
import PopoutControllers.ModifyEmployeeInformation;
import Singleton.FlightsAccess;
import Singleton.UserAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Login.Option;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * The Airport manager controller.
 */
public class AirportManagerController implements Initializable{
    // # # # FLIGHT TAB # # #
    // ADD FLIGHT
    @FXML
    public TextField flightnum;
    @FXML
    public DatePicker date;
    @FXML
    public TextField airline;
    @FXML
    public TextField destination;
    @FXML
    public TextField time;
    @FXML
    public Spinner<Integer> capacity;

    // Flight Table
    @FXML
    public TableView<Flight> flightTable;
    @FXML
    public TableColumn<Flight,String> flightCol;
    @FXML
    public TableColumn<Flight,String> airlineCol;
    @FXML
    public TableColumn<Flight,String> destinationCol;
    @FXML
    public TableColumn<Flight,Date> dateCol;
    @FXML
    public TableColumn<Flight,Time> timeCol;
    @FXML
    public TableColumn<Flight,Character> gateCol;

    // MISC
    @FXML
    public Button logoutbutton;
    @FXML
    public TextField searchBox;

    // # # # User Management Tab # # #
    // Add Users
    @FXML
    private TextField usersName;
    @FXML
    private TextField idNumberTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    public ComboBox<Option> selectionComboBox;
    @FXML
    public TextField employeeRoleTextField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label passMessageLabel;
    @FXML
    private Label errorMessageLabel;

    // User Table
    @FXML
    public TableView<User> tableviewUsers;
    @FXML
    public TableColumn<User,String> userIdCol;
    @FXML
    public TableColumn<User,String> userNameCol;
    @FXML
    public TableColumn<User,String> userRoleCol;
    @FXML
    public TextField searchUsers;

    // CheckBoxes
    @FXML
    private RadioButton passengerCheck;
    @FXML
    private RadioButton employeeCheck;
    @FXML
    private RadioButton allUsersCheck;


    /**
     * Logout as the Airport Manager.
     */
    @FXML
    public void logout(ActionEvent event) throws IOException
    {
        Parent loginViewParent = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginViewScene = new Scene(loginViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginViewScene);
        window.show();
    }

    /**
     * Checks the format of the inputted flight data for validity
     */
    @FXML
    private boolean checkFields(MouseEvent event) {
        boolean isValid = true;
        if (flightnum.getText().isEmpty() || airline.getText().isEmpty() || destination.getText().isEmpty() ||
                date.getValue() == null || time.getText().isEmpty()) {
            isValid = false;
            notifyError("the empty field(s)");
        }
        if (!flightnum.getText().matches("[0-9]+")) {
            isValid = false;
            notifyError("a valid flight number");
            flightnum.clear();
        }
        if (time.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            time.appendText(":00");
        }
        if (!time.getText().matches("^(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d)$")) {
            isValid = false;
            notifyError("a valid time");
            time.clear();
        }
        return isValid;
    }

    /**
     * A helper method that shows an error message to the user
     */
    private void notifyError(String errorInfo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid " + errorInfo);
        alert.setContentText("Please fill " + errorInfo);
        alert.showAndWait();
    }

    /**
     * Calls and executes the add flight command when the user clicks the button
     * @param event when the addFlight button is clicked
     */
    @FXML
    public void addFlight(ActionEvent event) {
        if (FlightsAccess.getInstance().stream().anyMatch(flight -> flight.getFlightNumber().equals(flightnum.getText()))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Flight with this number already exists!");
            alert.showAndWait();
        }
        else {
            AddFlight addflight = new AddFlight(this);
            if (checkFields(null)) {
                addflight.execute();
            }
        }
    }

    /**
     * Clears the form for adding flights
     */
    @FXML
    public void clearForm(ActionEvent event) {
        flightnum.clear();
        airline.clear();
        destination.clear();
        time.clear();
        date.setValue(null);
    }

    /**
     * Clears the form for adding users
     */
    @FXML
    public void clearUserForm(ActionEvent event) {
        usersName.clear();
        idNumberTextField.clear();
        setPasswordField.clear();
        confirmPasswordField.clear();
        selectionComboBox.setValue(null);
        employeeRoleTextField.clear();
        employeeRoleTextField.setVisible(false);
    }

    /**
     * Initializes the Controller.
     * This method is automatically called by JavaFx when the controller is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectionComboBox.setItems(FXCollections.observableArrayList(Option.values()));

        employeeRoleTextField.setVisible(false);

        // checks if comboBox selection is 'Airport Employee' to make role text field visible
        selectionComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> employeeRoleTextField.setVisible(newValue.equals(Option.AIRPORTEMPLOYEE))
                );

        // Initialize the columns in the flight table
        flightCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        gateCol.setCellValueFactory(new PropertyValueFactory<>("gate"));

        // Initialize the data for the table which is the list of flights in the FlightAccess
        flightTable.setItems(FlightsAccess.getSearchInstance());

        // Make the data in the flight table searchable
        searchBox.textProperty().addListener((observableValue,oldValue,newValue) -> FlightsAccess.getSearchInstance().setPredicate(Flight.search(newValue)));

        capacity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000));

        // Initialize the columns in the user table
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Initialize the data for the user table and make it searchable
        tableviewUsers.setItems(UserAccess.getSearchInstance());
        searchUsers.textProperty().addListener((observableValue,oldValue,newValue) -> UserAccess.getSearchInstance().setPredicate(User.search(newValue)));

        // Radio button to filter out everyone that isn't a passenger from the user management table
        passengerCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<User> actualList = tableviewUsers.getItems();
            FilteredList<User> items = new FilteredList<>(actualList);
            // if checked
            if(newValue){
                Predicate<User> isPassenger = i -> i.getRole().equals("Passenger");
                items.setPredicate(isPassenger);
                tableviewUsers.setItems(items);
            }
            else{
                tableviewUsers.setItems(UserAccess.getSearchInstance());
            }
        });

        // Radio button to filter out everyone that isn't a passenger from the user management table
        employeeCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<User> actualList = tableviewUsers.getItems();
            FilteredList<User> items = new FilteredList<>(actualList);
            // if checked
            if(newValue){
                Predicate<User> isNotPassenger = i -> !i.getRole().equals("Passenger");
                items.setPredicate(isNotPassenger);
                tableviewUsers.setItems(items);
            }
            else{
                tableviewUsers.setItems(UserAccess.getSearchInstance());
            }
        });

        // Radio button to view all users
        allUsersCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // if checked
            if(newValue){
                tableviewUsers.setItems(UserAccess.getSearchInstance());
            }
        });
    }

    /**
     * Handles the button click to register a new user
     */
    public void registerButtonOnAction(ActionEvent event){
        ObservableList<User> userList = tableviewUsers.getItems();
        boolean duplicateUser = false;

        if(this.idNumberTextField.getText().isEmpty() || this.setPasswordField.getText().isEmpty() || this.confirmPasswordField.getText().isEmpty()){
            this.errorMessageLabel.setText("Please make sure all fields are correctly filled out!");
            this.messageLabel.setText("");
            this.passMessageLabel.setText("");
            return;
        }
        if (this.selectionComboBox.getValue() == null) {
            this.errorMessageLabel.setText("Please select a user type!");
            this.messageLabel.setText("");
            this.passMessageLabel.setText("");
            return;
        }

        if(!this.usersName.getText().matches("[A-Za-z\\s]{2,}")){
            this.usersName.clear();
            this.errorMessageLabel.setText("Please enter a valid name!");
            this.messageLabel.setText("");
            this.passMessageLabel.setText("");
            return;
        }

        if(!this.idNumberTextField.getText().matches("^[a-zA-Z0-9]*$")){
            this.idNumberTextField.clear();
            this.errorMessageLabel.setText("Please enter a valid ID number!");
            this.messageLabel.setText("");
            this.passMessageLabel.setText("");
            return;
        }

        if(setPasswordField.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$") && setPasswordField.getText().equals(confirmPasswordField.getText())){
            Passenger e;
            if (selectionComboBox.getValue().equals(Option.PASSENGER))
                e = new Passenger(usersName.getText(), idNumberTextField.getText(), "");
            else
                e = new Employee(idNumberTextField.getText(), usersName.getText(), "");

            if(selectionComboBox.getValue().toString().compareTo(Option.AIRPORTEMPLOYEE.toString()) == 0) {

                if(employeeRoleTextField.getText().isEmpty())
                    e.setRole(selectionComboBox.getValue().toString());
                else
                    e.setRole(employeeRoleTextField.getText());

            } else {
                e.setRole(selectionComboBox.getValue().toString());
            }


            for(User user : userList){
                if (user.getId().equals(e.getId())) {
                    duplicateUser = true;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("User with this ID already exists!");
                    alert.showAndWait();
                    messageLabel.setText("");
                    errorMessageLabel.setText("");
                    passMessageLabel.setText("");
                }
            }

            if(!duplicateUser){
                new AddUser(e, setPasswordField.getText().toCharArray()).execute();

                clearUserForm(null);

                messageLabel.setText("User has been registered successfully!");
                errorMessageLabel.setText("");
                passMessageLabel.setText("");
            }
        }
        else {
            passMessageLabel.setText("Please make sure your passwords match and that it contains at least one uppercase, " +
                    "lowercase, and number");
            this.setPasswordField.clear();
            this.confirmPasswordField.clear();
            this.messageLabel.setText("");
            this.errorMessageLabel.setText("");
        }
    }

    /**
     * Deletes the selected flight from the system when the user pressed the button
     *
     * @param actionEvent the user selecting delete flight
     */
    public void deleteRow(ActionEvent actionEvent) {
        ObservableList<Flight> selectedFlights;
        if (!(selectedFlights = flightTable.getSelectionModel().getSelectedItems()).isEmpty()) {
            DeleteFlight deleteflight = new DeleteFlight(selectedFlights);
            deleteflight.execute();
        }
    }

    /**
     * Updates the flight row selected when the user presses the button
     *
     * @param actionEvent an action performed by the user
     */
    public void updateRow(ActionEvent actionEvent) {
        if (!(flightTable.getSelectionModel().getSelectedItems()).isEmpty()) {
            if (checkFields(null)) {
                Flight theFlight = flightTable.getSelectionModel().getSelectedItem();
                theFlight.setAirline(airline.getText());
                theFlight.setDestination(destination.getText());
                theFlight.setDate(Date.valueOf(date.getValue()));
                theFlight.setTime(Time.valueOf(time.getText()));

                new UpdateFlight(theFlight).execute();
                clearForm(null);
            }
        }
    }

    /**
     * Handles a double click on a flight in the table
     * Loads the flightView for the flight that was double clicked
     */
    @FXML
    public void doubleClickFlight(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ObservableList<Flight> selectedFlights;
            if (!(selectedFlights = flightTable.getSelectionModel().getSelectedItems()).isEmpty()) {
                openFlightView(selectedFlights.get(0));
            }
        }
    }

    /**
     * Open flight view for a flight that was double clicked in the table
     */
    public void openFlightView(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightView.fxml"));
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            loader.<FlightInfo>getController().initialize(flight);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Double click to modify employee or to add a task
     */
    @FXML
    public void doubleClickEmployee(MouseEvent event) {
        if (event.getClickCount() == 2) {
            User user;
            if ((user = tableviewUsers.getSelectionModel().getSelectedItem()) != null){
                if (user instanceof Employee) {
                    modifyEmployeeInformation((Employee) user);
                    addTask((Employee) user);
                }
            }
        }
    }

    /**
     * Open modify employee view.
     */
    public void modifyEmployeeInformation(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifyEmployeeInformation.fxml"));

            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            loader.<ModifyEmployeeInformation>getController().initialize(employee);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open daily task window.
     */
    public void addTask(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTasks.fxml"));
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            loader.<AddTasks>getController().initialize(employee);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
