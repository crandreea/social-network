package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.dto.UserFilterDTO;
import ubb.scs.map.service.NetworkService;
import ubb.scs.map.utils.PopupNotification;
import ubb.scs.map.utils.UserSession;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class HomeController implements Observer {

    private final NetworkService networkService = GlobalService.getNetwork();
    public TextField searchField;
    public ListView<String> userListView;
    public ImageView requestImage;
    public Text text;
    public Button btnPrevious;
    public Label currentPageLabel;
    public Button btnNext;
    @FXML
    private Utilizator selectedUser;
    private Utilizator currentUser;
    private UserSession userSession;

    private final UserFilterDTO filter = new UserFilterDTO();
    private int currentPage = 0;

    public void setUserSession(UserSession session) {
        userSession = session;
    }

    public void initialize() {
         currentUser = UserSession.getCurrentUser();
        loadUsers();
        searchField.textProperty().addListener(o -> {
            filter.setUsername(Optional.ofNullable(searchField.getText()));
            currentPage = 0;
            loadUsers();
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/layout-default.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("Nu s-a găsit fișierul FXML!");
            } else {
                Parent root = loader.load();
                HeaderController headerController = loader.getController();

                loader.setRoot(root);
                loader.setControllerFactory(param -> headerController);
                loader.load();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadUsers() {
        userListView.getItems().clear();
        int pageSize = 5;

        Page<Utilizator> userPage = networkService.findAllUsersOnPage(new Pageable(currentPage, pageSize), filter);

        int totalNumberOfPages = (int) Math.ceil((double) userPage.getTotalNumberOfElements() / pageSize) - 1;
        if (totalNumberOfPages <= -1) {
            totalNumberOfPages = 1;
        }
        if (currentPage > totalNumberOfPages) {
            currentPage = totalNumberOfPages;
            userPage = networkService.findAllUsersOnPage(new Pageable(currentPage, pageSize), filter);
        }

        btnPrevious.setDisable(currentPage == 0);
        btnNext.setDisable((currentPage + 1) * pageSize >= userPage.getTotalNumberOfElements());

        List<Utilizator> users = StreamSupport.stream(userPage.getElementsOnPage().spliterator(), false).toList();
        users.forEach(utilizator -> userListView.getItems().add(utilizator.getUsername()));
        currentPageLabel.setText("Page " + (currentPage + 1) + " of " + (totalNumberOfPages + 1));
    }


    @FXML
    private void handleUserSelection() {
        String selectedUsername = userListView.getSelectionModel().getSelectedItem();
        if (selectedUsername != null) {
            selectedUser = StreamSupport.stream(networkService.getAllUtilizatori().spliterator(), false)
                    .filter(user -> user.getUsername().equals(selectedUsername))
                    .findFirst()
                    .orElse(null);
        }
    }

    private boolean isAlreadyFriend(Utilizator selectedUser) {
        return StreamSupport.stream(networkService.getAllPrietenii().spliterator(), false)
                .anyMatch(friendship -> (friendship.getIdUser1().equals(currentUser.getId()) && friendship.getIdUser2().equals(selectedUser.getId())) ||
                        (friendship.getIdUser2().equals(currentUser.getId()) && friendship.getIdUser1().equals(selectedUser.getId())));
    }


    public void handleAddFriend() throws SQLException {
        Stage stage = (Stage) text.getScene().getWindow();
        handleUserSelection();
        if (selectedUser != null && !isAlreadyFriend(selectedUser)) {
            networkService.addPrietenie(currentUser.getId(), selectedUser.getId());
            Prietenie pr = new Prietenie(currentUser.getId(), selectedUser.getId());
            pr.setStatus("pending");
            PopupNotification.showNotification(stage, "Your request has been sent!!", 3000, "#52b8ff ");

        } else {
            PopupNotification.showNotification(stage, "You have already sent a request! Please be patient!", 3000, "#2aa8ff ");
        }
    }


    @Override
    public void update() {
        loadUsers();
    }

    public void handlePreviousPage(ActionEvent actionEvent) {
        currentPage--;
        loadUsers();
    }

    public void handleNextPage(ActionEvent actionEvent) {
        currentPage++;
        loadUsers();
    }
}

