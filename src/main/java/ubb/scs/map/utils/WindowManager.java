package ubb.scs.map.utils;

import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;

public class WindowManager {
    private static UserSession userSession;

    public static void setMainStage(Stage stage, UserSession session) {
        userSession = session;
        addCloseHandler(stage);
    }

    public static void addCloseHandler(Stage stage) {
        stage.setOnCloseRequest(event -> {
            if (userSession != null) {
                Utilizator currentUser = UserSession.getCurrentUser();
                if (currentUser != null) {
                    userSession.logoutUser(); // Folosim userSession pentru logout
                    System.out.println("User " + currentUser.getUsername() + " logged out.");
                }
            }
        });
    }

}
