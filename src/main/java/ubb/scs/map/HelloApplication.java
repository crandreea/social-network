package ubb.scs.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ubb.scs.map.utils.DBUtils;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.WindowManager;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            DBUtils.clearLoggedUsersOnStartup();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/demo/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMaximized(true);
        stage.setTitle("main_app");
        stage.setScene(scene);

        UserSession userSession = new UserSession(null); // Creezi sesiunea
        WindowManager.setMainStage(stage, userSession);
        stage.show();
    }
}