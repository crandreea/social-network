package ubb.scs.map.repository.database;

import ubb.scs.map.database.DBConnection;
import ubb.scs.map.domain.Notification;
import ubb.scs.map.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;

public class NotificationDBRepository extends AbstractDBRepository<Long, Notification> {

    private final Connection databaseConnection;

    public NotificationDBRepository(Validator<Notification> validator) {
        super(validator);
        databaseConnection = DBConnection.getInstance().getConnection();
    }

    @Override
    protected PreparedStatement findOneQuery(Long id) throws SQLException {
        String query = "SELECT * FROM notification WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM notification";
        return databaseConnection.prepareStatement(query);
    }

    @Override
    protected PreparedStatement saveQuery(Notification entity) throws SQLException {
        String query = "INSERT INTO notification (id, description, date, uid) VALUES (?,?, ?, ?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, entity.getId());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        preparedStatement.setObject(4, entity.getUserId());
        return preparedStatement;
    }

    @Override
    protected PreparedStatement deleteQuery(Long id) throws SQLException {
        String query = "DELETE FROM notification WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    protected PreparedStatement updateQuery(Notification entity) throws SQLException {
        String query = "UPDATE notification SET description = ?, date = ?, uid = ? WHERE id = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
        preparedStatement.setObject(3, entity.getUserId());
        preparedStatement.setObject(4, entity.getId());
        return preparedStatement;
    }

    @Override
    protected Notification buildEntity(ResultSet resultSet) throws SQLException {
        Long id = (Long) resultSet.getObject("id");
        String description = resultSet.getString("description");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        Long userId = (Long) resultSet.getObject("uid");

        Notification notification = new Notification(id, description, userId);
        notification.setDate(date);
        //notification.setId(id);
        return notification;
    }
}
