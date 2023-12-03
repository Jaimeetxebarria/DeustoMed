package org.deustomed.postgrest;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PostgrestQueryBuilder {
    private PostgrestQuery postgrestQuery = new PostgrestQuery();

    public PostgrestQuery getQuery() {
        return postgrestQuery;
    }

    public PostgrestQueryBuilder from(@NotNull String table) {
        if (table.isEmpty()) {
            throw new IllegalArgumentException("Cannot query blank table");
        }
        postgrestQuery.getUrlBuilder().setPath('/' + table);
        return this;
    }

    public PostgrestFilterBuilder select(@NotNull String... columns) {
        if (columns.length == 0 || (columns.length == 1 && columns[0].equals("*"))) {
            return new PostgrestFilterBuilder(this.postgrestQuery);
        }

        for (String column : columns) {
            if (column == null || column.isEmpty()) throw new IllegalArgumentException("Cannot query blank column");
        }

        postgrestQuery.getUrlBuilder().setQueryParameter("select", String.join(",", columns));
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }

    public PostgrestQueryBuilder insert(@NotNull Entry... entries) {
        if (entries.length == 0) {
            throw new IllegalArgumentException("Cannot insert empty entry list");
        }

        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append(String.join(",", Arrays.stream(entries)
                .map(entry -> "\"" + entry.getColumnName() + "\":\"" + entry.getValue() + "\"").toList()));
        body.append("}");

        postgrestQuery.setBody(body.toString());
        postgrestQuery.addHeader("Prefer", "missing=default");
        //headers.get("Prefer").add("return=representation");
        return this;
    }

}
