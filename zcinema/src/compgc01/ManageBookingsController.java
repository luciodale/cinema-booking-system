package compgc01;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class ManageBookingsController implements Initializable {

    int bookedSeatsCount;

    @FXML
    static Stage stage;
    @FXML
    GridPane gridSeats;
    @FXML
    Button backButton, giveFeedback;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> filmDropDownList, timeDropDownList, customerDropDownList;
    @FXML
    Label bookedSeatsLabel, availableSeatsLabel, totalSeatsLabel;
    @FXML
    Text customer;
    @FXML
    MaterialIconView A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, C1, C2, C3, C4, C5, C6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!Main.isEmployee()) {
            customerDropDownList.setVisible(false); 
            customer.setVisible(false);
            giveFeedback.setVisible(true);
        }
        

        // setting the date to the current one in the default time-zone of the system
        datePicker.setValue(LocalDate.now());
        try {
            populateFilmDropDownList(new ActionEvent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        populateTimeDropDownList(new ActionEvent());
        populateUserDropDownList(new ActionEvent());

        // setting the total number of seats to a value of 18
        totalSeatsLabel.setText("Total seats: 18");
        bookedSeatsCount = 0;
        Main.setSelectedSeats(new ArrayList<String>());

        // getting the most recent version of the bookings file
        Main.resetBookingList();
        Main.readJSONFile("bookingsJSON.txt");
        Main.resetEmployeeList();
        Main.readJSONFile("employeesJSON.txt");
        Main.resetCustomerList();
        Main.readJSONFile("customersJSON.txt");
        Main.resetFilmList();
        Main.readJSONFile("filmsJSON.txt");

        if (!Main.isEmployee()) {
            bookedSeatsLabel.setVisible(false);
            availableSeatsLabel.setVisible(false);
            totalSeatsLabel.setVisible(false);
        }

        // action that is fired whenever the time is changed
        timeDropDownList.setOnAction((event) -> {

            try {
                datePicker.getValue().equals(null);

                Main.getSelectedSeats().clear();

                Main.setSelectedTime(timeDropDownList.getValue());

                // resetting the number of booked seats for every date, film, and time
                bookedSeatsCount = 0;

                // resetting all seats to black every time the user selects a new screening time
                for (int i = 0; i < 18; i++) {
                    gridSeats.getChildren().get(i)
                    .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                }


                // spotting the booked seats for a specific film, date, and time and turning their colour to grey
                for (BookingHistoryItem booking : Main.getBookingList()) {
                    // making sure we do not include the cancelled bookings
                    if (booking.getStatus().equals("booked")) {
                        // checking if the booking's film, date, and time match the user's choice
                        if (booking.getDate().equals(datePicker.getValue().toString())
                                && booking.getFilm().equals(filmDropDownList.getValue())
                                && booking.getTime().equals(timeDropDownList.getValue())) {
                            // turning the booked seat grey
                            for (int i = 0; i < 18; i++) {
                                if (gridSeats.getChildren().get(i).getId().equals(booking.getSeat())) {
                                    gridSeats.getChildren().get(i).setStyle(
                                            "-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                                    // incrementing the count of the booked seats
                                    bookedSeatsCount++;
                                }
                            }
                        }
                    }
                }

                // setting the number of the booked seats and empty seats every time there is an action
                // and the specific screening (film, date, and time) changes
                bookedSeatsLabel.setText("Booked seats: " + bookedSeatsCount);
                availableSeatsLabel.setText("Available seats: " + (18 - bookedSeatsCount));

            } catch (NullPointerException ex) {
                ex.getStackTrace();
            }

        });
    }

    @FXML
    private void selectSeat(MouseEvent e) {

        // firing a pop up message if user clicks on already booked seat
        if (((Node) e.getSource()).getStyle()
                .equals("-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
            Alert alert = new Alert(AlertType.WARNING,
                    "The seat " + ((Node) e.getSource()).getId() + " is already booked!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            // turning seat back to black if it is red - unselecting it
            if (((Node) e.getSource()).getStyle()
                    .equals("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
                ((Node) e.getSource())
                .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                Main.getSelectedSeats().remove(((Node) e.getSource()).getId());
            }
            // turning seat red if it is black - selecting it
            else {
                ((Node) e.getSource())
                .setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                Main.getSelectedSeats().add(((Node) e.getSource()).getId());
            }
        }
    }

    /**
     * Method that gets called when a customer clicks on the book seat button to make a booking.
     * @param e Mouse event representing a click on the book seat button
     * @throws IOException Exception to be thrown if there is a problem with storing the booking in the JSON text files
     * @throws GeneralSecurityException Exception to be thrown if encryption fails
     */
    @FXML
    private void bookSeat(MouseEvent e) throws IOException, GeneralSecurityException {

        if (Main.getSelectedSeats().size() == 0){
            throwAlert();
            return;
        }
        try {
            datePicker.getValue().equals(null);
            filmDropDownList.getValue().equals(null);
            timeDropDownList.getValue().equals(null);
            if (Main.isEmployee())
                customerDropDownList.getValue().equals(null);
        } catch(NullPointerException ex){    		
            throwAlert();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to proceed with the booking?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.NO) {
            alert.close();
            return;
        }
        else {
            // getting the latest booking id and incrementing by one
            int newBookingId = Main.getBookingList().size() + 1;
            // System.out.println(newBookingId);
            //        System.out.println(customerDropDownList.getValue());

            for (int i = newBookingId; i < (newBookingId + Main.getSelectedSeats().size()); i++) {

                if(Main.isEmployee())
                    Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "username", Encryption.encrypt(customerDropDownList.getValue()));
                else
                    Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "username", Encryption.encrypt(Main.getCurrentUser().getUsername()));

                Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "date", datePicker.getValue().toString());
                Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "seat", Main.getSelectedSeats().get(i - newBookingId));
                Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "time", timeDropDownList.getValue());
                Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "film", filmDropDownList.getValue());
                Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "status", "booked");
            }

            Main.resetBookingList();
            Main.readJSONFile("bookingsJSON.txt");

            if (!Main.isEmployee()){
                SceneCreator.launchScene("/scenes/BookingSummaryScene.fxml");
                Main.getStage().centerOnScreen();
            } else {
                Alert alert1 = new Alert(AlertType.INFORMATION, "You have completed the booking for " + customerDropDownList.getValue() + "!", ButtonType.OK);
                alert1.showAndWait();
                if(alert1.getResult() == ButtonType.OK) {
                    alert1.close();
                    SceneCreator.launchScene("/scenes/ManageBookingsScene.fxml");
                }
            }
        }
    }

    static Stage getStage() {

        return stage;
    }

    @FXML
    private void showBookingHistoryOnClick(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/BookingHistoryScene.fxml");
    }
    
    @FXML
    private void giveFeedback(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/FeedbackScene.fxml");
    }

    @FXML
    private void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/UserScene.fxml");
    }

    /**
     * Method that gets called when a date is selected from the date picker.
     * @param event Action event representing a selection in the date picker
     * @throws ParseException Exception to be thrown if the parsing of film start and end dates to LacalDate objects fails
     */
    @FXML
    private void populateFilmDropDownList(ActionEvent event) throws ParseException {

        Main.resetFilmList();
        Main.readJSONFile("filmsJSON.txt");
        try {
            Main.setSelectedDate(datePicker.getValue().toString());

            ObservableList<String> filmTitles = FXCollections.observableArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Film film : Main.getFilmList()) {
                if ((LocalDate.parse(film.getStartDate(), formatter).isBefore(datePicker.getValue()) ||
                        LocalDate.parse(film.getStartDate(), formatter).equals(datePicker.getValue()))
                        && (LocalDate.parse(film.getEndDate(), formatter).isAfter(datePicker.getValue()) ||
                                LocalDate.parse(film.getEndDate(), formatter).equals(datePicker.getValue())))
                    filmTitles.add(film.getTitle());
            }

            filmDropDownList.setItems(filmTitles);
        }
        catch(NullPointerException e) {
            e.getMessage();
        }
    }

    /**
     * Method that gets called when a film is selected from the drop-down list.
     * @param event Action event representing a selection in the films' drop-down list
     */
    @FXML
    private void populateTimeDropDownList(ActionEvent event) {

        try {
            Main.setSelectedFilmTitle(filmDropDownList.getValue());
            Film selectedFilm = Main.getFilmByTitle(Main.getSelectedFilmTitle());

            ObservableList<String> timesList = FXCollections.observableArrayList(selectedFilm.getTimes());
            for (int i = 0; i< timesList.size(); i++) {
                if (timesList.get(i).equals("hh:mm")) {
                    timesList.remove(i);
                    i--;
                }
            }

            timeDropDownList.setItems(timesList);
        }
        catch (NullPointerException ex) {
            return;
        }
    }

    @FXML
    private void populateUserDropDownList(ActionEvent event) {

        try {
            ObservableList<String> customersList = FXCollections.observableArrayList();
            for(Customer c : Main.getCustomerList()){
                customersList.add(c.getUsername());
            }
            customerDropDownList.setItems(customersList);
        } catch(NullPointerException e) {
            return;
        }
    }

    @FXML
    private void throwAlert() {

        Alert alert = new Alert(AlertType.INFORMATION, "Please make sure all fields are selected!", ButtonType.OK);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK){
            alert.close();
        }
    }
}