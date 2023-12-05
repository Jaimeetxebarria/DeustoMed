package org.deustomed.postgrest;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Entry {
    private final String columnName;
    private final String value;

    public Entry(@NotNull String columnName, @NotNull String value) {
        if (columnName.isEmpty()) throw new IllegalArgumentException("Cannot query blank column");

        this.columnName = columnName;
        this.value = value;
    }

    public Entry(@NotNull String columnName, @NotNull Integer value) {
        this(columnName, value.toString());
    }

    public String getColumnName() {
        return columnName;
    }

    public String getValue() {
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
}
