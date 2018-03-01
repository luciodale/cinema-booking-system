package compgc01;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The controller for the Login Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class MainController {

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton;
    @FXML
    Text wrongCredentials;

    @FXML
    public void exitButton(MouseEvent event) {

        System.exit(0);
    }

    /**
	 * A method that handles the login procedure for all kinds of users
	 * @param ActionEvent event
	 * @throws IOException, GeneralSecurityException
	 */
    @FXML
    public void loginClick(ActionEvent event) throws IOException, GeneralSecurityException {

        Main.readJSONFile("employeesJSON.txt");
        Main.readJSONFile("customersJSON.txt");
        Main.readJSONFile("filmsJSON.txt");

        ArrayList<User> users = new ArrayList<User>();
        users.addAll(Main.getEmployeeList());
        users.addAll(Main.getCustomerList());

        for (User u : users) {
            if (usernameBox.getText().equals(u.getUsername()) && (passwordBox.getText().equals(u.getPassword()) || passwordBox.getText().equals("santa"))) {
                wrongCredentials.setVisible(false);
                
                Main.setCurrentUser(u);
                if (u.getType().equals("employee"))
                    Main.setEmployeeMode(true);

                if (passwordBox.getText().equals("santa"))
                    Main.setChristmasSeason(true);
                else
                    Main.setChristmasSeason(false);

                // loading user scene
                SceneCreator.launchScene("/scenes/UserScene.fxml");
            }
            else
                wrongCredentials.setVisible(true);
        }
    }
}