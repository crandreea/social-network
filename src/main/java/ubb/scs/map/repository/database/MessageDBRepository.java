package ubb.scs.map.repository.database;

import ubb.scs.map.database.DBConnection;
import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    private final Connection connection;

    public MessageDBRepository(Validator<Message> validator) {
        super(validator);
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    protected PreparedStatement findOneQuery(Long id) throws SQLException {
        String query = "SELECT * FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, java.util.UUID.fromString(String.valueOf(id)));
        return preparedStatement;
    }

    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM messages";
        return connection.prepareStatement(query);
    }

    @Override
    protected PreparedStatement saveQuery(Message entity) throws SQLException {
        String query = "INSERT INTO messages(id, sid, rid, message, date) VALUES (?,?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, entity.getId());
        statement.setObject(2, entity.getFromId());
        statement.setObject(3, entity.getToId());
        statement.setObject(4, entity.getMessage());
        statement.setObject(5, entity.getDate());
        return statement;
    }

    @Override
    protected PreparedStatement deleteQuery(Long id) throws SQLException {
        String query = "DELETE FROM messages WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, id);
        return preparedStatement;
    }

    @Override
    protected PreparedStatement updateQuery(Message entity) throws SQLException {
        String query = "UPDATE messages SET sid = ?, rid = ?, message = ?, date = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, entity.getFromId());
        preparedStatement.setObject(2, entity.getToId());
        preparedStatement.setObject(3, entity.getMessage());
        preparedStatement.setObject(4, entity.getDate());
        return preparedStatement;
    }

    @Override
    protected Message buildEntity(ResultSet resultSet) throws SQLException {
        Long id = (Long) resultSet.getObject("id");
        Long sender_id = (Long) resultSet.getObject("sid");
        Long  receiver_id = (Long) resultSet.getObject("rid");
        String messageText = resultSet.getString("message");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

        Message message = new Message(sender_id, receiver_id, messageText);
        message.setDate(date);
        message.setId(id);
        return message;
    }

    public List<Message> findMessagesByUsers(Long senderId, Long receiverId) throws SQLException {
        List<Message> messages = new ArrayList<>();

        try (PreparedStatement statement = getSentQuery(senderId, receiverId)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = buildEntity(resultSet);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messages;
    }

    private PreparedStatement getSentQuery(Long senderId, Long receiverId) throws SQLException {
        String query = "SELECT * FROM messages WHERE sid = ? AND rid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, senderId);
        preparedStatement.setObject(2, receiverId);
        return preparedStatement;
    }


}
