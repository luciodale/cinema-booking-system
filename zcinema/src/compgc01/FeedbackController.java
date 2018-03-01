package compgc01;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * The controller class for the Feedback Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 15.12.2017
 */
public class FeedbackController implements Initializable {

    @FXML
    ComboBox<String> filmDropDownList;
    @FXML
    String user = Main.getCurrentUser().getFullName();
    @FXML
    TextArea feedbackComment;
    @FXML
    Button backButton, submitButton;
    @FXML
    MaterialDesignIconView star1, star2, star3, star4, star5;
    @FXML
    ImageView smileFace, neutralFace, angryFace, smileGlow, neutralGlow, angryGlow;
    @FXML
    String starNumber, experience, film, comment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        submitButton.setDisable(true);

        try {
            populateFilmDropDownList(new ActionEvent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendFeedback (ActionEvent e) throws IOException {

        SceneCreator.launchScene("/scenes/UserScene.fxml");

        setStars();
        setExperience();
        setFilm();
        setComment();

        SendEmail.sendEmail("uclcinemaapp@gmail.com", "feedback");
    }

    @FXML
    private void enableSubmitButton() {

        if (starNumber != null && experience != null && filmDropDownList.getValue() != null)
            submitButton.setDisable(false);
        else
            return;
    }

    @FXML
    private void populateFilmDropDownList(ActionEvent event) throws ParseException {

        Main.resetFilmList();
        Main.readJSONFile("filmsJSON.txt");

        try {

            ObservableList<String> filmTitles = FXCollections.observableArrayList();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Film film : Main.getFilmList()) {

                for (BookingHistoryItem item : Main.getBookingList()) {

                    LocalDate bookingDate = LocalDate.parse(item.getDate(), formatter);

                    if (film.getTitle().equals(item.getFilm())
                            && user.equals(item.getFirstName() + " " + item.getLastName())
                            && currentDate.compareTo(bookingDate) > 0) {

                        filmTitles.add(film.getTitle());

                    }
                }
            }
            if (filmTitles.size() == 0) {
                filmDropDownList.setDisable(true);
            } else {

                //removing duplicates
                Set<String> hs = new HashSet<>();
                hs.addAll(filmTitles);
                filmTitles.clear();
                filmTitles.addAll(hs);

            }
            filmDropDownList.setItems(filmTitles);
        } catch (NullPointerException e) {
            e.getMessage();
        }
        if (filmDropDownList.isDisable()) {
            feedbackComment.setDisable(true);
            submitButton.setDisable(true);
            alert();
        }
    }

    @FXML
    void alert() {
        Alert alert = new Alert(AlertType.INFORMATION, "You cannot leave feedback before watching a movie!",
                ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK)
            alert.close();
    }

    @FXML
    private void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("/scenes/ManageBookingsScene.fxml");
    }

    @FXML
    private void selectStars(MouseEvent e) {

        if (((Node) e.getSource()).getStyle().equals("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;"))
            switch (((Node) e.getSource()).getId().toString()) {
            case "star5":
                star5.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star4":
                star4.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star3":
                star3.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star2":
                star2.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star1":
                star1.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
                break;
            }
        else {
            switch (((Node) e.getSource()).getId().toString()) {
            case "star1":
                star1.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star2":
                star2.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star3":
                star3.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star4":
                star4.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
            case "star5":
                star5.setStyle("-fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");
                break;
            }
        }

        if (((Node) e.getSource()).getId().toString().equals("star1"))
            star1.setStyle("-fx-fill:yellow; -fx-font-family: 'Material Design Icons'; -fx-font-size: 20.0;");

        this.starNumber = ((Node) e.getSource()).getId().toString();
        enableSubmitButton();
    }

    @FXML
    private void selectExperience(MouseEvent e) {

        smileGlow.setVisible(false);
        neutralGlow.setVisible(false);
        angryGlow.setVisible(false);

        switch (((Node) e.getSource()).getId().toString()) {
        case "smileFace":
            smileGlow.setVisible(true);
            break;
        case "neutralFace":
            neutralGlow.setVisible(true);
            break;
        case "angryFace":
            angryGlow.setVisible(true);
        }

        this.experience = ((Node) e.getSource()).getId().toString();
        enableSubmitButton();

    }

    public void setStars() {

        Main.stars = starNumber;
    }

    public void setExperience() {

        Main.experience = experience;
    }

    public void setFilm() {

        try {
            Main.feedbackFilmTitle = filmDropDownList.getValue().toString();
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    public void setComment() {

        Main.comment = feedbackComment.getText().toString();
    }
}