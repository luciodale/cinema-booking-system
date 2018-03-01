package compgc01;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The controller for the Edit Info Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class EditInfoController implements Initializable {

	@FXML
	Button backButton;
	@FXML
	Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
	@FXML
	Label firstNameLabelNew, lastNameLabelNew, titleLabelNew, emailLabelNew;
	@FXML
	TextField updateFirstName, updateLastName, updateEmail, updatePassword;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			personaliseScene();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method updates the new user's information on the fly. They are displayed on the top right 
	 * side of the page
	 * @param KeyEvent e, being any key of the keyboard
	 */
	@FXML
	public void editUpdateText(KeyEvent e) {

		TextField field = (TextField) e.getSource();
		if (field.getText().length() > 30)
			field.setEditable(false);

		if (e.getCode().equals(KeyCode.BACK_SPACE))
			field.setEditable(true);

		switch (((Node) e.getSource()).getId()) {
		case "updateFirstName":
			firstNameLabelNew.setText(updateFirstName.getText());
			break;
		case "updateLastName":
			lastNameLabelNew.setText(updateLastName.getText());
			break;
		case "updateEmail":
			emailLabelNew.setText(updateEmail.getText());
			break;
		}
	}

	/**
	 * This method saves the new user's information in the JSON files. It will not update the data if
	 * the TextField in question contains only spaces.
	 * @param ActionEvent event, being the click on the designed button
	 * @throws IOException, GeneralSecurityException, the second being linked to the Encryption features 
	 */
	@FXML
	public void saveClick(ActionEvent event) throws IOException, GeneralSecurityException {

		String userType = Main.getCurrentUser().getType();

		Alert alertConf = new Alert(AlertType.CONFIRMATION, "Are you sure you want to update your information?", ButtonType.NO, ButtonType.YES);
		alertConf.showAndWait();
		if(alertConf.getResult() == ButtonType.NO){
			alertConf.close();
			return;
		} else {
			if (!updateEmail.getText().trim().isEmpty()) {
				if(emailValidator()){
					Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "email", updateEmail.getText());
					Main.getCurrentUser().setEmail(updateEmail.getText());
				}
				else {
					Alert alert = new Alert(AlertType.WARNING, "The email must be of this format: \"example01@ucl.com\"!", ButtonType.OK);
					alert.showAndWait();
					if(alert.getResult() == ButtonType.OK){
						return;
					}
				}
			}
			if (!updateFirstName.getText().trim().isEmpty()) {
				Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "firstName", updateFirstName.getText());
				Main.getCurrentUser().setFirstName(updateFirstName.getText());
			}
			if (!updateLastName.getText().trim().isEmpty()) {
				Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "lastName", updateLastName.getText());
				Main.getCurrentUser().setLastName(updateLastName.getText());
			}

			if (!updatePassword.getText().trim().isEmpty()) {
				String encryptedPassword = Encryption.encrypt(updatePassword.getText());
				Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "password", encryptedPassword);
				Main.getCurrentUser().setPassword(updatePassword.getText());
			}
			alertConf.close();
			SceneCreator.launchScene("/scenes/UserScene.fxml");
		}
	}

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("/scenes/UserScene.fxml");
	}

	protected void personaliseScene() throws IOException {

		// personalising page based on logged-in user
		firstNameLabel.setText(Main.getCurrentUser().getFirstName());
		lastNameLabel.setText(Main.getCurrentUser().getLastName());
		if (!Main.isEmployee()) {
			titleLabel.setText("Customer");
			windowTitleLabel.setText("Edit " + titleLabel.getText() + " Profile");
		}
		emailLabel.setText(Main.getCurrentUser().getEmail());
		titleLabelNew.setText(titleLabel.getText());

		firstNameLabelNew.setText(firstNameLabel.getText());
		lastNameLabelNew.setText(lastNameLabel.getText());
		emailLabelNew.setText(emailLabel.getText());
	}


	@FXML
	boolean emailValidator () {
		return Pattern.matches("[A-Za-z0-9/.]+([/@])[A-Za-z0-9]+[/.][A-Za-z/.]+", (CharSequence) updateEmail.getText());
	}

}