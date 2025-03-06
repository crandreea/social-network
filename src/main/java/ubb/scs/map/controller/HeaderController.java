package ubb.scs.map.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.utils.UserSession;


public class HeaderController{
    public ImageView profileImage;
    public ImageView homeImage;
    public ImageView messagesImage;
    public ImageView logoutImage;
    public ImageView notificationImage;

    @FXML
    private TextField usernameHeaderField;

    private UserSession userSession;
    private Utilizator currentUser;

    public void setUserSession(UserSession session) {
      userSession = session;
    }

    public void initialize() {
        setUserSession(userSession);

       try{
           currentUser = UserSession.getCurrentUser();
           String username = currentUser.getUsername();
           if(username != null) {
           usernameHeaderField.setText(username + "!");
           usernameHeaderField.setVisible(true);
           }
       }catch(Exception e){
           e.printStackTrace();
       }


    }

    public void gotToMessages(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/messages.fxml"));
            Parent signUpRoot = loader.load();

            Messages messagesController = loader.getController();
            messagesController.setUserSession(userSession);

            Stage MessageStage = (Stage) messagesImage.getScene().getWindow();
            MessageStage.setScene(new Scene(signUpRoot));
            MessageStage.setMaximized(true);
            MessageStage.setTitle("Chat");
            MessageStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotToLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Parent loginRoot = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Login");
            loginStage.setMaximized(true);
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotToHome(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/home-view.fxml"));
            Parent signUpRoot = loader.load();

            HomeController messagesController = loader.getController();
            messagesController.setUserSession(userSession);


            Stage stage = (Stage) profileImage.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.setMaximized(true);
            stage.setTitle("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToProfile(MouseEvent mouseEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/profile-view.fxml"));
            Parent signUpRoot = loader.load();

            ProfileView messagesController = loader.getController();
            messagesController.setUserSession(userSession);

            Stage stage = (Stage) profileImage.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.setMaximized(true);
            stage.setTitle("Profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToNotification(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/notification-view.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) notificationImage.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.setMaximized(true);
            stage.setTitle("Notifications");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}