package org.deustomed.postgrest;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostgrestFilterBuilder extends PostgrestTransformBuilder {
    private PostgrestQuery postgrestQuery;
    private boolean negateNextFilter = false;

    public PostgrestFilterBuilder() {
        super(new PostgrestQuery());
        postgrestQuery = super.getQuery();
    }

    public PostgrestFilterBuilder(PostgrestQuery postgrestQuery) {
        super(postgrestQuery);
        this.postgrestQuery = postgrestQuery;
    }

    public PostgrestQuery getQuery() {
        return postgrestQuery;
    }

    /**
     * Equal to
     */
    public PostgrestFilterBuilder eq(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "eq", value);
        return this;
    }

    /**
     * Not equal to
     */
    public PostgrestFilterBuilder neq(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "neq", value);
        return this;
    }

    /**
     * Less than
     */
    public PostgrestFilterBuilder lt(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "lt", value);
        return this;
    }

    /**
     * Greater than
     */
    public PostgrestFilterBuilder gt(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "gt", value);
        return this;
    }


    /**
     * Greater than or equal to
     */
    public PostgrestFilterBuilder gte(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "gte", value);
        return this;
    }

    /**
     * Less than or equal to
     */
    public PostgrestFilterBuilder lte(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "lte", value);
        return this;
    }

    /**
     * Like
     */
    public PostgrestFilterBuilder like(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "like", value);
        return this;
    }

    /**
     * Ilike (case-insensitive like)
     */
    public PostgrestFilterBuilder ilike(@NotNull String column, @NotNull String value) {
        this.addFilter(column, "ilike", value);
        return this;
    }

    /**
     * Is checks for null, true or false
     */
    public PostgrestFilterBuilder is(@NotNull String column, Boolean value) {
        if (value == null) this.addFilter(column, "is", "null");
        else this.addFilter(column, "is", value.toString());

        return this;
    }

    /**
     * In
     */
    // TODO filter values for reserved characters: https://postgrest.org/en/v7.0.0/api.html#reserved-characters
    public PostgrestFilterBuilder in(@NotNull String column, @NotNull String... values) {
        if (values.length == 0) throw new IllegalArgumentException("Cannot use in filter with no values");
        if (Arrays.stream(values).anyMatch(value -> value == null || value.isEmpty()))
            throw new IllegalArgumentException("Cannot use in filter with empty values");

        this.addFilter(column, "in",
                "(" + Arrays.stream(values).map(s -> "\"" + s + "\"").collect(Collectors.joining(",")) + ")");
        return this;
    }

    public PostgrestFilterBuilder in(@NotNull String column, @NotNull Integer... values) {
        if (values.length == 0) throw new IllegalArgumentException("Cannot use in filter with no values");
        if (Arrays.stream(values).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Cannot use in filter with empty values");

        this.addFilter(column, "in",
                "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(",")) + ")");
        return this;
    }

    /** Negate the next filter */
    public PostgrestFilterBuilder not() {
        negateNextFilter = true;
        return this;
    }

    private void addFilter(@NotNull String column, @NotNull String filter, String value) {
        if (value == null) value = "null";
        if (negateNextFilter) {
            filter = "not." + filter;
            negateNextFilter = false;
        }
        postgrestQuery.getUrlBuilder().addQueryParameter(column, filter + "." + value);
    }

}
