package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import ubb.scs.map.domain.Notification;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.service.UserService;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;

import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.StreamSupport;

public class NotificationController implements Observer {

    private NetworkService network;
    private Iterable<Notification> allNotifications = List.of();
    private final ObservableList<String> notifications = FXCollections.observableArrayList();

    @FXML
    private ListView<String> notificationsList;

    @FXML
    private Text noNotificationsText;

    @FXML
    private Button confirmBtn;

    @FXML
    public void initialize() {
        network = GlobalService.getNetwork();
        network.addObserver(this);
        loadNotifications();
        notificationsList.setItems(notifications);
    }


    @Override
    public void update() {
        loadNotifications();
    }

    private void loadNotifications() {
        allNotifications = network.getUserNotifications(UserSession.getCurrentUser().getId());

        List<String> plainTextNotifications = StreamSupport.stream(allNotifications.spliterator(), false)
                .map(Notification::getDescription)
                .toList();
        notifications.setAll(plainTextNotifications);

        if (plainTextNotifications.isEmpty()) {
            notificationsList.setVisible(false);
            notificationsList.setManaged(false);
            confirmBtn.setVisible(false);
            //confirmBtn.setManaged(false);
            noNotificationsText.setVisible(true);
            noNotificationsText.setManaged(true);
        } else {
            notificationsList.setVisible(true);
            notificationsList.setManaged(true);
            confirmBtn.setVisible(true);
            confirmBtn.setManaged(true);
            noNotificationsText.setVisible(false);
            noNotificationsText.setManaged(false);
        }
    }

    public void handleMarkNotifications() {
        StreamSupport.stream(allNotifications.spliterator(), false)
                .forEach(notification -> {
                    System.out.println(notification);
                    network.deleteNotification(notification.getId());
                })
                ;
    }
}
