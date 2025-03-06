package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.dto.UserFilterDTO;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;


public class ProfileView implements Observer {
    private final NetworkService networkService = GlobalService.getNetwork();
    public AnchorPane header;
    public TextField usernameField;
    public TextField nmbFriendsField;
    public ImageView deleteFriendImage;
    public ImageView acceptRequestImage;
    public ImageView deleteRequestImage;
    public ListView<String> friendsListView;
    public ListView<String> pendingRequestsListView;
    public Button btnPrevious;
    public Label currentPageLabel;
    public Button btnNext;
    private Utilizator currentUser;
    @FXML
    private Prietenie selectedFriendRequest;



    private UserSession userSession;
    public void setUserSession(UserSession session) {
        userSession = session;
    }

    public void initialize() {
        currentUser = UserSession.getCurrentUser();
        String username = currentUser.getUsername();


        if (username != null) {
            usernameField.setText(username);
            usernameField.setVisible(true);
        }
        update();

        pendingRequestsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleRequest(newValue);
        });

    }

    private void loadPendingFriends() {
        pendingRequestsListView.getItems().clear();

        StreamSupport.stream(networkService.getAllPrietenii().spliterator(), false)
                .filter(friendship ->
                        ((friendship.getIdUser2().equals(currentUser.getId()))) && Objects.equals(friendship.getStatus(), "pending"))
                .forEach(friendship -> {
                    Long friendId = (friendship.getIdUser1().equals(currentUser.getId())) ? friendship.getIdUser2() : friendship.getIdUser1();

                    String friendUsername = StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                            .filter(user -> user.getId().equals(friendId))
                            .map(Utilizator::getUsername)
                            .findFirst()
                            .orElse("Unknown");
                    LocalDate date = friendship.getDate();
                    pendingRequestsListView.getItems().add(friendUsername + "   requested on:" + date);

                });
    }


    @FXML
    private void loadFriends() {
        friendsListView.getItems().clear();

        StreamSupport.stream(networkService.getAllPrietenii().spliterator(), false)
                .filter(friendship ->
                        (friendship.getIdUser1().equals(currentUser.getId()) || friendship.getIdUser2().equals(currentUser.getId())))
                .forEach(friendship -> {
                    Long friendId = (friendship.getIdUser1().equals(currentUser.getId())) ? friendship.getIdUser2() : friendship.getIdUser1();

                    String friendUsername = StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                            .filter(user -> user.getId().equals(friendId))
                            .map(Utilizator::getUsername)
                            .findFirst()
                            .orElse("Unknown");
                    LocalDate date = friendship.getDate();
                    String status = friendship.getStatus();
                    if (status.equals("accepted")) {
                        friendsListView.getItems().add(friendUsername + "   friends from:" + date);
                    }
                });

        nmbFriendsField.setText(numberOfFriends());
    }

    public String numberOfFriends(){
        return String.valueOf(friendsListView.getItems().size());
    }

    public void deleteFriend(MouseEvent actionEvent) {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            Long friendId = getFriendIdByUsername(selectedFriend.split("   friends from:")[0]);

            if (friendId != null) {
                networkService.deletePrietenie(currentUser.getId(), friendId);
                update();
            }
        }
        String selectedFriendRequest = pendingRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedFriendRequest != null) {
            Long friendId = getFriendIdByUsername(selectedFriendRequest.split("   requested on:")[0]);

            if (friendId != null) {
                networkService.deletePrietenie(currentUser.getId(), friendId);
                update();
            }
        }
    }

    private Long getFriendIdByUsername(String username) {
        return StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                .filter(user -> user.getUsername().equals(username))
                .map(Utilizator::getId)
                .findFirst()
                .orElse(null);
    }

    private void handleRequest(String newValue) {
        String selectedFriend = pendingRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            selectedFriendRequest = (StreamSupport.stream(networkService.getAllPrietenii().spliterator(), false)
                    .filter(friendship ->
                            (friendship.getIdUser2().equals(currentUser.getId())))
                    .findFirst()
                    .orElse(null));

        }
    }

    public void acceptRequestFriend(MouseEvent actionEvent) {
        if (selectedFriendRequest != null) {
            networkService.acceptFriendRequest(selectedFriendRequest);
            update();
        }
    }

    @Override
    public void update() {
        loadFriends();
        loadPendingFriends();
    }


}
