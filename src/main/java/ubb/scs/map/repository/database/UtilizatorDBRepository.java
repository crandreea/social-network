package ubb.scs.map.repository.database;

import javafx.util.Pair;
import ubb.scs.map.database.DBConnection;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.dto.UserFilterDTO;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.paging.PagingRepository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UtilizatorDBRepository extends AbstractDBRepository<Long, Utilizator> implements PagingRepository<Long, Utilizator> {
    private final Connection connection;

    public UtilizatorDBRepository(UtilizatorValidator validator) {
        super(validator);
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    protected PreparedStatement findOneQuery(Long id) throws SQLException {
        String query = "SELECT * FROM utilizatori WHERE uid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, id);

        return statement;
    }

    @Override
    protected PreparedStatement findAllQuery() throws SQLException {
        String query = "SELECT * FROM utilizatori";
        PreparedStatement statement = connection.prepareStatement(query);

        return statement;
    }

    @Override
    protected PreparedStatement saveQuery(Utilizator entity) throws SQLException {
        String query = "INSERT INTO utilizatori(uid, username, password) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, entity.getId());
        statement.setString(2, entity.getUsername());
        statement.setString(3, entity.getPassword());

        return statement;
    }

    @Override
    protected PreparedStatement deleteQuery(Long id) throws SQLException {
        String query = "DELETE FROM utilizatori WHERE uid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setObject(1, id);

        return statement;
    }

    @Override
    protected PreparedStatement updateQuery(Utilizator entity) throws SQLException {
        return null;
    }

    @Override
    protected Utilizator buildEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("uid");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");

        Utilizator user = new Utilizator(username, password);
        user.setId(id);

        return user;
    }

    private Pair<String, List<Object>> toSQL(UserFilterDTO filter) {

        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }

        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        filter.getUsername().ifPresent(usernameFilter -> {
            conditions.add("username LIKE ?");
            params.add("%" + usernameFilter + "%");
        });

        String query = String.join(" AND ", conditions);
        return new Pair<>(query, params);
    }

    private int count(UserFilterDTO filter) {
        String query = "SELECT COUNT(*) AS count FROM utilizatori";
        Pair<String, List<Object>> SQLFilter = toSQL(filter);

        if (!SQLFilter.getKey().isEmpty()) {
            query += " WHERE " + SQLFilter.getKey();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int paramIndex = 0;
            for (Object param : SQLFilter.getValue()) {
                preparedStatement.setObject(++paramIndex, param);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private List<Utilizator> findAllUsersOnPage(Pageable pageable, UserFilterDTO filter) {
        List<Utilizator> usersOnPage = new ArrayList<>();

        String query = "SELECT * FROM utilizatori";
        Pair<String, List<Object>> SQLFilter = toSQL(filter);

        if (!SQLFilter.getKey().isEmpty()) {
            query += " WHERE " + SQLFilter.getKey();
        }
        query += " LIMIT ? OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int paramIndex = 0;
            for (Object param : SQLFilter.getValue()) {
                preparedStatement.setObject(++paramIndex, param);
            }

            preparedStatement.setInt(++paramIndex, pageable.getPageSize());
            preparedStatement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    usersOnPage.add(buildEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usersOnPage;
    }

    public Page<Utilizator> findAllOnPage(Pageable pageable, UserFilterDTO filter) {
        int totalNumberOfRows = count(filter);
        List<Utilizator> usersOnPage;

        if (totalNumberOfRows > 0) {
            usersOnPage = findAllUsersOnPage(pageable, filter);
        } else {
            usersOnPage = new ArrayList<>();
        }

        return new Page<>(usersOnPage, totalNumberOfRows);
    }

    @Override
    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
    }

}
