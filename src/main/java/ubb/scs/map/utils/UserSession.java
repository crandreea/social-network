package ubb.scs.map.utils;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.utils.DBUtils;

import java.sql.SQLException;
import java.util.*;

public class UserSession {
    private static Utilizator currentUser;
    private static final Set<Utilizator> loggedUsers = new HashSet<>();

    public UserSession(Utilizator initialUser) {
        currentUser = initialUser;
    }

    public static void setCurrentUser(Utilizator currentUser) {
        UserSession.currentUser = currentUser;
    }


    public static boolean loginUser(Utilizator user) throws SQLException {
        if (loggedUsers.contains(user)) {
            return false;
        }

        loggedUsers.add(user);
        DBUtils.addLoggedUser(user.getUsername());
        return true;
    }

    public static Utilizator getCurrentUser() {
        return currentUser;
    }

    public Set<Utilizator> getLoggedUsers() {
        return loggedUsers;
    }

    public void logoutUser() {
        Utilizator user = currentUser;
        if (user != null) {
            loggedUsers.remove(user);
            try {
                DBUtils.removeLoggedUser(user.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


