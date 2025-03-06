package ubb.scs.map.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.observer.Observer;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class Messages implements Observer {
    public AnchorPane header;
    public ListView<String> friendsList;
    private Iterable<Message> allMessages = List.of();

    public TextField searchField;
    public VBox chatBox;
    public TextArea messageField;
    public ScrollPane messagesScroller;
    private Utilizator currentUser;
    private Optional<Utilizator> selectedFriend;
    @FXML
    private Prietenie selectedFriendRequest;

    private NetworkService networkService;
    private UserSession userSession;

    public void setUserSession(UserSession session) {
        userSession = session;
    }

    public void initialize() {
        networkService = GlobalService.getNetwork();
        currentUser = UserSession.getCurrentUser();
        update();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
        friendsList.setOnMouseClicked(event -> {
                    String selectedItem = friendsList.getSelectionModel().getSelectedItem();
                    selectedFriend = networkService.getUserByUsername(selectedItem);
                    if(selectedItem!=null){
                        searchField.setText(selectedItem);
                        loadMessages(selectedFriend);
                        Platform.runLater(this::scrollToBottom);
                    }
                });

        chatBox.heightProperty().addListener((observable, oldValue, newValue) -> scrollToBottom());
    }


    private void filterUsers(String query) {
        List<String> allPrietenii = networkService.getFriendsOfUser(currentUser);
        friendsList.getItems().clear();
        if (query.isEmpty()) {
            update();
        } else {
            List<String> filteredUsers = allPrietenii.stream()
                    .filter(user -> true)
                    .filter(user -> user.toLowerCase().contains(query.toLowerCase()))
                    .toList();

            friendsList.getItems().addAll(filteredUsers);
        }
    }

    @FXML
    private void loadFriends() {
        friendsList.getItems().clear();

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
                        friendsList.getItems().add(friendUsername);
                    }
                });
        friendsList.setOnMouseClicked(event -> onFriendSelected());
    }

    @FXML
    private void onFriendSelected() {
        String friendUsername = friendsList.getSelectionModel().getSelectedItem();
        if(friendUsername != null) {
            selectedFriend = Optional.ofNullable(StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                    .filter(user -> user.getUsername().equals(friendUsername))
                    .findFirst().orElse(null));
        }
    }


    private void loadMessages(Optional<Utilizator> friend) {
        chatBox.getChildren().clear();

        List<Message> messages = networkService.getMessagesBetweenUsers(currentUser, friend).stream().sorted(Comparator.comparing(Message::getDate))
                .toList();
        List<Node> messageNodes = messages.stream()
                .map(message -> {
                    Label messageLabel = new Label(message.getMessage());
                    messageLabel.getStyleClass().add("message-label");
                    messageLabel.setWrapText(true);
                    messageLabel.setMaxWidth(300);
                    messageLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);


                    HBox messageContainer = new HBox(messageLabel);
                    messageContainer.setMaxWidth(Double.MAX_VALUE);
                    messageContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    messageContainer.setFillHeight(true);

                    if (Objects.equals(message.getFromId(), currentUser.getId())) {
                        messageContainer.setAlignment(CENTER_RIGHT);
                        messageLabel.setStyle("-fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 7px 14px 7px 14px;\n" +
                                "    -fx-wrap-text: true;-fx-background-color: #90d2ff; -fx-text-alignment: left;");

                    } else {
                        messageContainer.setAlignment(CENTER_LEFT);
                        messageLabel.setStyle("-fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 7px 14px 7px 14px;\n" +
                                "    -fx-wrap-text: true;-fx-background-color: #57baff ; -fx-text-alignment: right;");

                    }

                    return messageContainer;
                })
                .collect(Collectors.toList());

        chatBox.getChildren().addAll(messageNodes);
        Platform.runLater(this::scrollToBottom);
    }

    @FXML
    private void sendMessage() {
        String messageText = messageField.getText();
        if (!messageText.isEmpty() && selectedFriend.isPresent()) {
            networkService.sendMessage(currentUser.getId(), selectedFriend.get().getId(), messageText);
            loadMessages(selectedFriend);
            messageField.clear();
        }
    }

    private void scrollToBottom() {
            Platform.runLater(() -> messagesScroller.setVvalue(1.0));

    }


    @Override
    public void update() {
        loadFriends();
    }
}
