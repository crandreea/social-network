package ubb.scs.map.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private Text helloText;

    @FXML
    private Text continueText;

    @FXML
    public void initialize() {
        FadeTransition fadeHello = new FadeTransition(Duration.seconds(2.2), helloText);
        fadeHello.setFromValue(0);
        fadeHello.setToValue(1);
        fadeHello.setCycleCount(1);
        fadeHello.setOnFinished(event -> {
            FadeTransition fadeContinue = new FadeTransition(Duration.seconds(1.5), continueText);
            fadeContinue.setFromValue(0);
            fadeContinue.setToValue(1);
            fadeContinue.setCycleCount(1);
            fadeContinue.play();
        });

        fadeHello.play();
    }
    public void handleMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Parent loginSignupRoot = loader.load();

            Stage stage = (Stage) ((Parent) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginSignupRoot));
            stage.setMaximized(true);
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}