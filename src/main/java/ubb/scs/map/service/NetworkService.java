package ubb.scs.map.service;

import ubb.scs.map.domain.*;
import ubb.scs.map.domain.dto.UserFilterDTO;
import ubb.scs.map.domain.validators.MessageValidator;
import ubb.scs.map.repository.database.MessageDBRepository;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NetworkService implements Observable {
    private final Service<Long, Utilizator> userService;
    private final Service<Tuplu<Long, Long>, Prietenie> prietenieService;
    private final Service<Long, Notification> notificationService;
    private final List<Observer> observers = new ArrayList<>();

    public NetworkService(Service<Long, Utilizator> userService, Service<Tuplu<Long, Long>, Prietenie> prietenieService, Service<Long, Notification> notificationService, MessageService messageService) {
        this.userService = userService;
        this.prietenieService = prietenieService;
        this.notificationService = notificationService;
    }

    public Long getNewUserId() {
        return StreamSupport.stream(userService.findAll().spliterator(), false)
                .mapToLong(Utilizator::getId)
                .max()
                .orElse(0L) + 1;
    }

    public Long getNewMessageId() {
        UUID randomId = UUID.randomUUID();
        return Math.abs(randomId.getMostSignificantBits());
    }

    public Long getNewNotificationId() {
        UUID randomId = UUID.randomUUID();
        return Math.abs(randomId.getMostSignificantBits())%10000;
    }

    public void addUtilizator(Utilizator utilizator) {
        utilizator.setId(getNewUserId());
        userService.save(utilizator);
        notifyObservers();
    }


    public void deleteUtilizator(Long id) {
        Optional<Utilizator> utilizatorsters = userService.delete(id);

        if (utilizatorsters.isPresent()) {
            StreamSupport.stream(prietenieService.findAll().spliterator(), false)
                    .filter(friendship -> friendship.containsUser(id))
                    .forEach(friendship -> {
                        prietenieService.delete(friendship.getId());
                    });
        }

        StreamSupport.stream(notificationService.findAll().spliterator(), false)
                .filter(notification -> Objects.equals(notification.getUserId(), id))
                .forEach(notification -> deleteNotification(notification.getId()));

        notifyObservers();
    }


    public void addPrietenie(Long id1, Long id2) throws SQLException {
        Prietenie pr = new Prietenie(id1, id2);
        pr.setId(new Tuplu<>(id1, id2));
        pr.setStatus("pending");

        Optional<Utilizator> sender = findUser(id1);
        String notificationDescription = "Request from " + sender.get().getUsername();
        Long id = getNewNotificationId();
        Notification notification = new Notification(id, notificationDescription, id2);

        notificationService.save(notification);
        System.out.println(notification);
        prietenieService.save(pr);
        notifyObservers();
    }

    public void deletePrietenie(Long id1, Long id2) {

        prietenieService.delete(new Tuplu<>(id1, id2));
        prietenieService.delete(new Tuplu<>(id2, id1));
        notifyObservers();
    }


    public void acceptFriendRequest(Prietenie prietenie) {
        prietenie.setStatus("accepted");
        prietenieService.update(prietenie);
        notifyObservers();
    }

    public Optional<Utilizator> findUser(Long id) throws SQLException {
        return userService.findOne(id);
    }

    public Iterable<Utilizator> getAllUtilizatori() {
        return userService.findAll();
    }

    public Iterable<Prietenie> getAllPrietenii() {
        return prietenieService.findAll();
    }


    public List<String> getFriendsOfUser(Utilizator currentUser){
        List<String> listOfFriends = new ArrayList<>();
        StreamSupport.stream(prietenieService.findAll().spliterator(), false)
                .filter(friendship ->
                        (friendship.getIdUser1().equals(currentUser.getId()) || friendship.getIdUser2().equals(currentUser.getId())))
                .forEach(friendship -> {
                    Long friendId = (friendship.getIdUser1().equals(currentUser.getId())) ? friendship.getIdUser2() : friendship.getIdUser1();

                    String friendUsername = StreamSupport.stream(userService.findAll().spliterator(), false)
                            .filter(user -> user.getId().equals(friendId))
                            .map(Utilizator::getUsername)
                            .findFirst()
                            .orElse("Unknown");
                    String status = friendship.getStatus();
                    if (status.equals("accepted")) {
                        listOfFriends.add(friendUsername);
                    }
                });
        return listOfFriends;
    }

    public List<Message> getMessagesBetweenUsers(Utilizator user1, Optional<Utilizator> user2) {
        MessageDBRepository messageDBRepository = new MessageDBRepository(new MessageValidator());
        List<Message> messages = new ArrayList<>();

        try {
            messages.addAll(messageDBRepository.findMessagesByUsers(user1.getId(), user2.get().getId()));

            messages.addAll(messageDBRepository.findMessagesByUsers(user2.get().getId(), user1.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public void sendMessage(Long fromId, Long toId, String messageText) {
        Message message = new Message(fromId, toId, messageText);
        message.setId(getNewMessageId());
        message.setDate(LocalDateTime.now());
        MessageDBRepository messageDBRepository = new MessageDBRepository(new MessageValidator());

        messageDBRepository.save(message);
        notifyObservers();

    }


    public Optional<Utilizator> getUserByUsername(String username) {
        return StreamSupport.stream(userService.findAll().spliterator(), false)
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }


    public Optional<Notification> deleteNotification(Long id) {
        Optional<Notification> deletedNotification = notificationService.delete(id);
        System.out.println(deletedNotification);
        notifyObservers();
        return deletedNotification;
    }

    public Iterable<Notification> getAllNotifications() {
        return notificationService.findAll();
    }

    public Iterable<Notification> getUserNotifications(Long uid) {
        return StreamSupport.stream(getAllNotifications().spliterator(), false)
                .filter(notification -> Objects.equals(notification.getUserId(), uid))
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public Page<Utilizator> findAllUsersOnPage(Pageable pageable) {
        return ((UserService) userService).findAllOnPage(pageable);
    }

    public Page<Utilizator> findAllUsersOnPage(Pageable pageable, UserFilterDTO filter) {
        return ((UserService) userService).findAllOnPage(pageable, filter);
    }
}
