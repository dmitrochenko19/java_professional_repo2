package ru.otus.jdbc.mapper;

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

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        Set<Field> fieldsWithIdAnnotation = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) != null).collect(Collectors.toSet());
        if (fieldsWithIdAnnotation.size() != 1) {
            throw new IllegalArgumentException("You can't create more than 1 id field!");
        }
        return fieldsWithIdAnnotation.stream().findFirst().get();
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> allFields = new ArrayList<>(getAllFields());
        allFields.remove(getIdField());
        return allFields;
    }
}
