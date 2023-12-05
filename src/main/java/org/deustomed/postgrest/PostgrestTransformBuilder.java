package org.deustomed.postgrest;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PostgrestTransformBuilder {
    private PostgrestQuery postgrestQuery;

    public PostgrestTransformBuilder() {
        postgrestQuery = new PostgrestQuery();
    }

    public PostgrestTransformBuilder(PostgrestQuery postgrestQuery) {
        this.postgrestQuery = postgrestQuery;
    }

    public PostgrestQuery getQuery() {
        return postgrestQuery;
    }

    public PostgrestTransformBuilder select() {
        postgrestQuery.addHeader("Prefer", "return=representation");
        return this;
    }

    public PostgrestTransformBuilder select(@NotNull String... columns) {
        if (columns.length == 0 || (columns.length == 1 && columns[0].equals("*"))) return select();

        if (Arrays.stream(columns).anyMatch(column -> column == null || column.isEmpty()))
            throw new IllegalArgumentException("Cannot query blank column");

        postgrestQuery.getUrlBuilder().setQueryParameter("select", String.join(",", columns));
        postgrestQuery.addHeader("Prefer", "return=representation");
        return this;
    }

    public PostgrestTransformBuilder order(@NotNull String column, boolean ascending) {
        if (column.isEmpty()) throw new IllegalArgumentException("Cannot order by blank column");
        String directionString = ascending ? "asc" : "desc";

        if (postgrestQuery.getUrlBuilder().hasQueryParameter("order")) {
            String preexistingOrder = postgrestQuery.getUrlBuilder().getQueryParameter("order");
            postgrestQuery.getUrlBuilder().setQueryParameter("order",
                    preexistingOrder + ',' + column + '.' + directionString);
            return this;
        }

        postgrestQuery.getUrlBuilder().setQueryParameter("order", column + '.' + directionString);
        return this;
    }

    public PostgrestTransformBuilder limit(int limit) {
        if (limit < 0) throw new IllegalArgumentException("Cannot limit by negative number");
        postgrestQuery.getUrlBuilder().setQueryParameter("limit", String.valueOf(limit));
        return this;
    }

    public PostgrestTransformBuilder offset(int offset) {
        if (offset < 0) throw new IllegalArgumentException("Cannot offset by negative number");
        postgrestQuery.getUrlBuilder().setQueryParameter("offset", String.valueOf(offset));
        return this;
    }

}
