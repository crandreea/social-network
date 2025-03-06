package ubb.scs.map.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.service.Authenticator;
import ubb.scs.map.utils.PopupNotification;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.WindowManager;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    public Text signUpText;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Utilizator currentUser;
    public void handleConfirmLoginClick() {
        String username = usernameField.getText().strip();
        String password = passwordField.getText().strip();

        Stage stage = (Stage) usernameField.getScene().getWindow();

        try{
            Utilizator user = Authenticator.login(username, password);

            if (user != null) {
                UserSession.setCurrentUser(user);
                //currentUser = user;
                UserSession userSession = new UserSession(user);
                //UserSession.loginUser(user);
                if (!UserSession.loginUser(user)) {
                    PopupNotification.showNotification(stage, "User already logged in!", 3000, "#ef5356");
                    return;
                }

                try {
                    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/com/example/demo/home-view.fxml"));
                    Parent newRoot = fxmlloader.load();

                    HomeController homeController = fxmlloader.getController();
                    homeController.setUserSession(userSession);

                    stage.setScene(new Scene(newRoot));
                    stage.setMaximized(true);
                    WindowManager.addCloseHandler(stage);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                PopupNotification.showNotification(stage, "Invalid username or password", 3000, "#ef5356");
            }
        }catch (ValidationException e){
            PopupNotification.showNotification(stage, e.getMessage(), 3000, "#ef5356");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleSignUpClick(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/sign_up.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) ((Text) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.setMaximized(true);
            stage.setTitle("Sign Up");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleMouseEnter(MouseEvent mouseEvent) {
        signUpText.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 13));
    }

    public void handleMouseExit(MouseEvent mouseEvent) {
        signUpText.setFont(Font.font("Arial", javafx.scene.text.FontWeight.NORMAL, 13));

    }
}
