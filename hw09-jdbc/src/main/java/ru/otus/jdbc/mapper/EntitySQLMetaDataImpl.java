package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from %s".formatted(entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from %s where %s = ?".formatted(
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        StringBuilder stringBuilder = new StringBuilder("insert into %s ".formatted(entityClassMetaData.getName()));
        StringBuilder stringBuilder1 = new StringBuilder("values (");
        stringBuilder.append("(");
        for (Field field : fields) {
            stringBuilder.append(field.getName()).append(",");
            stringBuilder1.append("?,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder1.deleteCharAt(stringBuilder1.length() - 1);
        stringBuilder.append(") ");
        stringBuilder1.append(")");
        stringBuilder.append(stringBuilder1);
        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder stringBuilder = new StringBuilder("update ").append(entityClassMetaData.getName()).append(" set ");
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fields) {
            stringBuilder.append(field.getName()).append(" = ").append("?,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" where ").append(entityClassMetaData.getIdField().getName()).append(" = ?");
        return stringBuilder.toString();
    }
}
