package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.Authenticator;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.utils.PasswordManager;
import ubb.scs.map.utils.PopupNotification;

public class SignUpController {
    @FXML
    public Text loginText;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField confirmedPasswordField;

    public void handleLoginClick() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) loginText.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.setMaximized(true);
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleMouseEnter() {
        loginText.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 13));
    }

    public void handleMouseExit() {

        loginText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
    }

    @FXML
    public void handleConfirmSignUpClick() {
        String username = usernameField.getText().strip();
        String password = passwordField.getText().strip();
        String confirmedPassword = confirmedPasswordField.getText().strip();

        Stage stage = (Stage) usernameField.getScene().getWindow();

        NetworkService network = GlobalService.getNetwork();
        Utilizator searchedUser = Authenticator.signUp(username, password, confirmedPassword);

        if (searchedUser == null) {
            try {
                String hashpass = PasswordManager.hashPassword(password);
                Utilizator user = new Utilizator(username, hashpass);
                network.addUtilizator(user);

                PopupNotification.showNotification(stage, "Account created successfully", 4000, "#68c96d");

                handleLoginClick();
            } catch (Exception e) {
                PopupNotification.showNotification(stage, e.getMessage(), 4000, "#ef5356");
            }
        } else {
            PopupNotification.showNotification(stage, "Username already exists", 4000, "#ef5356");
        }
    }
}
