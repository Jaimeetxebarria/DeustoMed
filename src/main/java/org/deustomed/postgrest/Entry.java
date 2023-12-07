package org.deustomed.postgrest;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class Entry<T> {
    private final String columnName;
    private final T value;

    public Entry(@NotNull String columnName, @NotNull T value) {
        if (columnName.isEmpty()) throw new IllegalArgumentException("Cannot query blank column");

        this.columnName = columnName;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry entry)) return false;
        return this.columnName.equals(entry.columnName) && this.value.equals(entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, value);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "columnName='" + columnName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static JsonObject toJsonObject(Entry<?>... entries) {
        if (entries.length == 0) throw new IllegalArgumentException("Cannot convert 0 entries to JSON");
        if (Arrays.stream(entries).anyMatch(Objects::isNull)) throw new IllegalArgumentException("Cannot convert null entry to JSON");

        JsonObject jsonObject = new JsonObject();
        for (Entry<?> entry : entries) {
            if (entry.getValue() instanceof Integer) jsonObject.addProperty(entry.getColumnName(), (Integer) entry.getValue());
            else if (entry.getValue() instanceof String) jsonObject.addProperty(entry.getColumnName(), (String) entry.getValue());
            else if (entry.getValue() instanceof Boolean) jsonObject.addProperty(entry.getColumnName(), (Boolean) entry.getValue());
            else if (entry.getValue() instanceof Double) jsonObject.addProperty(entry.getColumnName(), (Double) entry.getValue());
            else if (entry.getValue() instanceof Float) jsonObject.addProperty(entry.getColumnName(), (Float) entry.getValue());
            else if (entry.getValue() instanceof Long) jsonObject.addProperty(entry.getColumnName(), (Long) entry.getValue());
            else if (entry.getValue() instanceof Character) jsonObject.addProperty(entry.getColumnName(), (Character) entry.getValue());
            else throw new IllegalArgumentException("Cannot convert Entry with value of class " +
                        entry.getValue().getClass().getName() + " to JSON");
        }
        return jsonObject;
    }
}
