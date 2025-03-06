package ubb.scs.map.utils;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupNotification {
    public static void showNotification(Stage stage, String message, int durationInMillis, String backgroundColor) {
        Popup popup = new Popup();

        Label label = new Label(message);

        label.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-text-fill: #f0f0f0; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-font-size: 15px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px;"
        );

        popup.getContent().add(label);

        double xPosition = stage.getX() + stage.getWidth() / 2 - 100;
        double yPosition = stage.getY() + 20;

        popup.show(stage, xPosition, yPosition);

        new Thread(() -> {
            try {
                Thread.sleep(durationInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            javafx.application.Platform.runLater(popup::hide);
        }).start();
    }
}
