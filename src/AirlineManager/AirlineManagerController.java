package AirlineManager;

import Command.AddFlight;
import Command.DeleteFlight;
import Command.UpdateFlight;
import Entities.Flight;
import Singleton.flightsAccess;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AirlineManagerController implements Initializable {

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
    public Button logoutbutton;
    @FXML
    public TableView<Flight> tableview;
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
    @FXML
    public TextField searchBox;

    private ObservableList<Flight> data;

    @FXML
    public void logout(ActionEvent event) throws IOException
    {
        Parent loginViewParent = FXMLLoader.load(getClass().getResource("/loginapp/login.fxml"));
        Scene loginViewScene = new Scene(loginViewParent);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(loginViewScene);
        window.show();
    }

    @FXML
    private void checkTime(MouseEvent event) {
        if (time.getText().matches("^(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d)$")) {
        }
        else if (time.getText().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))
            time.appendText(":00");
        else{
            time.clear();
        }
    }

    @FXML
    public void addFlight(ActionEvent event) {
        AddFlight addflight = new AddFlight(this);
        addflight.execute();
    }

    @FXML
    private void searchTable(KeyEvent event) {
        if(searchBox.getText().isBlank()) {
            tableview.setItems(flightsAccess.getInstance());
        }
        else {
            tableview.setItems(flightsAccess.search(searchBox.getText()));
        }
    }


    @FXML
    public void clearForm(ActionEvent event) {
        flightnum.clear();
        airline.clear();
        destination.clear();
        time.clear();
        date.setValue(null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flightCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        gateCol.setCellValueFactory(new PropertyValueFactory<>("gate"));

        tableview.setItems(flightsAccess.getInstance());
    }

    public void deleteRow(ActionEvent actionEvent) {
        ObservableList<Flight> selectedFlights;
        if (!(selectedFlights = tableview.getSelectionModel().getSelectedItems()).isEmpty()) {
            DeleteFlight deleteflight = new DeleteFlight(selectedFlights);
            deleteflight.execute();
        }
    }


    public void updateRow(ActionEvent actionEvent) throws SQLException {
        ObservableList<Flight> selectedFlights;
        if (!(selectedFlights = tableview.getSelectionModel().getSelectedItems()).isEmpty()) {
            UpdateFlight updateFlight = new UpdateFlight(selectedFlights, this);
            updateFlight.execute();
        }
    }

    public void loadFLightData() throws SQLException {

        try {

            Connection connection = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM flights");

            while(rs.next()) {
                this.data.add(new Flight(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getTime(5), rs.getInt(6)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.flightCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        this.airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));
        this.destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        this.tableview.setItems(null);
        this.tableview.setItems(this.data);
    }
}
