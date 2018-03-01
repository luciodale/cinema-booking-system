package compgc01;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

/**
 * A class taking the information from the ManageBookingsController to ultimately display 
 * a summary of the user's booking information.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */

public class BookingSummaryController implements Initializable {

    @FXML
    Text nameSummary, filmSummary, dateSummary, timeSummary, seatSummary;
    @FXML
    ToggleButton closeButton, emailButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nameSummary.setText((Main.getCurrentUser().getFullName()));		
        filmSummary.setText(Main.getSelectedFilmTitle());
        dateSummary.setText(Main.getSelectedDate());
        timeSummary.setText(Main.getSelectedTime());
        for (int i = 0; i < Main.getSelectedSeats().size(); i++) {
            seatSummary.setText(seatSummary.getText() + Main.getSelectedSeats().get(i) + " ");
        }
    }

    @FXML
    private void closeStage(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/ManageBookingsScene.fxml");
        Main.getStage().centerOnScreen();
    }

    /**
	 * This method sends an email to the customer every time they make a booking so that they will have
	 * their booking information safely saved in their inbox folder
	 * @param ActionEvent event
	 */
    @FXML
    private void emailReminder(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION, "Would you like a confirmation to be emailed to you?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            SendEmail.sendEmail(Main.getCurrentUser().getEmail(), "reminder");
            alert.close();
        }
        else {
            alert.close();
            return;
        }
    }
}