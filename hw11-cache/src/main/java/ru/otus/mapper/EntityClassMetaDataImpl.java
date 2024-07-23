package ru.otus.mapper;

import ru.otus.crm.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<?> clazz;
    private  Constructor<?> constructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        if (constructor == null) {
            try {
                constructor = clazz.getDeclaredConstructor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            Set<Field> fieldsWithIdAnnotation = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.getAnnotation(Id.class) != null).collect(Collectors.toSet());
            if (fieldsWithIdAnnotation.size() != 1) {
                throw new IllegalArgumentException("You can't create more than 1 id field!");
            }
            idField = fieldsWithIdAnnotation.stream().findFirst().get();
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = Arrays.stream(clazz.getDeclaredFields()).toList();
        }
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null)
        {
            List<Field> all = new ArrayList<>(getAllFields());
            all.remove(getIdField());
            fieldsWithoutId = all;
        }
        return fieldsWithoutId;
    }
}
