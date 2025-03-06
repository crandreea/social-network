package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    public AbstractDBRepository(Validator<E> validator) {
    }


    protected abstract PreparedStatement findOneQuery(ID id) throws SQLException;

    protected abstract PreparedStatement findAllQuery() throws SQLException;

    protected abstract PreparedStatement saveQuery(E entity) throws SQLException;

    protected abstract PreparedStatement deleteQuery(ID id) throws SQLException;

    protected abstract PreparedStatement updateQuery(E entity) throws SQLException;

    protected abstract E buildEntity(ResultSet resultSet) throws SQLException;

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!");
        }

        try (PreparedStatement statement = findOneQuery(id)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    E entity = buildEntity(resultSet);
                    entity.setId(id);
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        List<E> entities = new ArrayList<>();

        try (PreparedStatement statement = findAllQuery()) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    //ID id = (ID) resultSet.getObject("uid");
                    E entity = buildEntity(resultSet);
                    //entity.setId(id);
                    entities.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity-ul nu poate fi null!");
        }

        try (PreparedStatement statement = saveQuery(entity)) {
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    ID id = (ID) resultSet.getObject("uid");
                    entity.setId(id);
                }
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!");
        }

        Optional<E> entity = findOne(id);
        if (entity.isPresent()) {
            try (PreparedStatement statement = deleteQuery(id)) {
                statement.executeUpdate();
                return entity;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity-ul nu poate fi null!");
        }

        Optional<E> entity_up = findOne(entity.getId());
        if (entity_up.isPresent()) {
            try (PreparedStatement statement = updateQuery(entity)) {
                statement.executeUpdate();
                return entity_up;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

}
