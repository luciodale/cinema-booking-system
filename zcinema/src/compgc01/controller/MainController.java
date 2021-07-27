package compgc01.controller;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import compgc01.model.Main;
import compgc01.model.SceneCreator;
import compgc01.model.User;
import compgc01.service.AutenticacaoServico;
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
    
    private AutenticacaoServico autenticacaoServico = new AutenticacaoServico();

    /**
	 * A method that handles the login procedure for all kinds of users
	 * @param ActionEvent event
	 * @throws IOException, GeneralSecurityException
	 */
    @FXML
    public void loginClick(ActionEvent event) throws IOException, GeneralSecurityException {
   
        try {
			User user = autenticacaoServico.autenticar(usernameBox.getText(), 
					passwordBox.getText());
			Main.setCurrentUser(user);
			Main.setToken(autenticacaoServico.getToken());
			SceneCreator.launchScene("/scenes/UserScene.fxml");
		} catch (IOException | ParseException e) {
			System.out.print(e);
			wrongCredentials.setVisible(true);
		}
    }
}