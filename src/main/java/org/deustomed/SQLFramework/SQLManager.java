package org.deustomed.SQLFramework;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLManager {
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

    public SQLManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        //connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }


    /**
     * Returns all fields of a class, including the fields of the superclasses (not including Object)
     * and private fields.
     */
    private List<Field> getAllFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(List.of(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    public void insert(Object object) throws IllegalArgumentException {
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(SQLTable.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @SQLTable");
        }

        var tableName = clazz.getAnnotation(SQLTable.class).name();

        var annotatedFields = this.getAllFields(clazz).stream().filter(field -> field.getAnnotation(SQLField.class) != null).toList();

        List<String> fieldNames = new ArrayList<>(annotatedFields.size());
        List<String> fieldValues = new ArrayList<>(annotatedFields.size());

        for(Field field : annotatedFields){
            field.setAccessible(true);
            fieldNames.add(field.getAnnotation(SQLField.class).name());
            try {
                fieldValues.add(field.get(object).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        System.out.println("INSERT INTO " + tableName + " (" + String.join(",", fieldNames) + ") VALUES (" + String.join(",", fieldValues)   + ")");

    }

}