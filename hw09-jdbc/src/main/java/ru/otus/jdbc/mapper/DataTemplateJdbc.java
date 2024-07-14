package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return (Optional<T>) dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), (Function<ResultSet, Object>) resultSet -> {
            try {
                if (resultSet.next()) {
                    return createObject(resultSet);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return (List<T>) dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), (Function<ResultSet, Object>) resultSet -> {
            List<T> res = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    res.add(createObject(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return res;
        }).orElse(null);
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(client));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(client));
            }
            Field id = entityClassMetaData.getIdField();
            id.setAccessible(true);
            params.add(id.get(client));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    private T createObject(ResultSet resultSet) {
        try {
            T object = entityClassMetaData.getConstructor().newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(object, resultSet.getObject(field.getName()));
            }
            return object;
        } catch (SQLException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
