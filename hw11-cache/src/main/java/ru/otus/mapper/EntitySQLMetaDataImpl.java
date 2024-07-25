package ru.otus.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;
    private String selectAllQuery;
    private String selectByIdQuery;
    private String updateQuery;
    private String insertQuery;

    private static final String SELECT_ALL_QUERY = "select * from %s";
    private static final String SELECT_BY_ID_QUERY = "select * from %s where %s = ?";
    private static final String INSERT_INTO_PART = "insert into %s ";
    private static final String UPDATE_QUERY_PART = "update";
    private static final String SET_QUERY_PART = "set";
    private static final String VALUES_PART = "values";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String EMPTY_SPACE = " ";
    private static final String QUESTION = "?";
    private static final String EQUALS = " = ";
    private static final String WHERE_PART = "where";
    private static final String COMMA = ",";

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllQuery == null) {
            selectAllQuery = SELECT_ALL_QUERY.formatted(entityClassMetaData.getName());
        }
        return selectAllQuery;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdQuery == null) {
            selectByIdQuery = SELECT_BY_ID_QUERY.formatted(entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
        }
        return selectByIdQuery;
    }

    @Override
    public String getInsertSql() {
        if (insertQuery == null) {
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            StringBuilder stringBuilder = new StringBuilder(INSERT_INTO_PART.formatted(entityClassMetaData.getName()));
            StringBuilder stringBuilder1 = new StringBuilder(VALUES_PART).append(EMPTY_SPACE).append(OPEN_BRACKET);
            stringBuilder.append(OPEN_BRACKET);
            for (Field field : fields) {
                stringBuilder.append(field.getName()).append(COMMA);
                stringBuilder1.append(QUESTION).append(COMMA);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder1.deleteCharAt(stringBuilder1.length() - 1);
            stringBuilder.append(CLOSE_BRACKET).append(EMPTY_SPACE);
            stringBuilder1.append(CLOSE_BRACKET);
            stringBuilder.append(stringBuilder1);
            insertQuery = stringBuilder.toString();
        }
        return insertQuery;
    }

    @Override
    public String getUpdateSql() {
        if (updateQuery == null) {
            StringBuilder stringBuilder = new StringBuilder(UPDATE_QUERY_PART).append(EMPTY_SPACE).append(entityClassMetaData.getName()).append(EMPTY_SPACE).append(SET_QUERY_PART).append(EMPTY_SPACE);
            List<Field> fields = entityClassMetaData.getFieldsWithoutId();
            for (Field field : fields) {
                stringBuilder.append(field.getName()).append(EQUALS).append(QUESTION).append(COMMA);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(EMPTY_SPACE).append(WHERE_PART).append(EMPTY_SPACE)
                    .append(entityClassMetaData.getIdField().getName()).append(EQUALS).append(QUESTION);
            updateQuery = stringBuilder.toString();
        }
        return updateQuery;
    }
}
