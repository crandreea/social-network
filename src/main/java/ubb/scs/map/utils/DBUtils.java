package ubb.scs.map.utils;

import ubb.scs.map.database.DBConnection;

import java.sql.*;

public class DBUtils {
    private static final Connection connection = DBConnection.getInstance().getConnection();;

    public DBUtils() {
    }

    public static boolean isUserLoggedIn(String username) throws SQLException {
        String query = "SELECT username FROM logged_users WHERE username = ?";

        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    public static void addLoggedUser(String username) throws SQLException {
        if (isUserLoggedIn(username)) return;
        String query = "INSERT INTO logged_users(username) VALUES(?)";

        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

    public static void removeLoggedUser(String username) throws SQLException {
        String query = "DELETE FROM logged_users WHERE username = ?";

        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.executeUpdate();

    }

    public static void clearLoggedUsersOnStartup() throws SQLException {
        String query = "DELETE FROM logged_users";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
            System.out.println("All logged users have been cleared on startup.");
        }
    }

}