package org.deustomed.postgrest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.httputils.HttpMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PostgrestQueryBuilder {
    private PostgrestQuery postgrestQuery;

    public PostgrestQueryBuilder() {
        postgrestQuery = new PostgrestQuery();
    }

    public PostgrestQueryBuilder(PostgrestQuery postgrestQuery) {
        this.postgrestQuery = postgrestQuery;
    }

    public PostgrestQuery getQuery() {
        return postgrestQuery;
    }

    public PostgrestFilterBuilder select(@NotNull String... columns) {
        if (columns.length == 0 || (columns.length == 1 && columns[0].equals("*"))) {
            return new PostgrestFilterBuilder(this.postgrestQuery);
        }

        if (Arrays.stream(columns).anyMatch(column -> column == null || column.isEmpty()))
            throw new IllegalArgumentException("Cannot query blank column");

        postgrestQuery.setHttpMethod(HttpMethod.GET);
        postgrestQuery.getUrlBuilder().setQueryParameter("select", String.join(",", columns));
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }

    public PostgrestFilterBuilder insert(@NotNull JsonElement jsonElement, boolean defaultToNull) {
        postgrestQuery.setHttpMethod(HttpMethod.POST);
        postgrestQuery.setBody(jsonElement);
        if (!defaultToNull) postgrestQuery.addHeader("Prefer", "missing=default");
        postgrestQuery.addHeader("Content-Type", "application/json");
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }

    public PostgrestFilterBuilder insert(@NotNull JsonElement jsonElement) {
        postgrestQuery.setHttpMethod(HttpMethod.POST);
        postgrestQuery.setBody(jsonElement);
        postgrestQuery.addHeader("Prefer", "missing=default");
        postgrestQuery.addHeader("Content-Type", "application/json");
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }


    public PostgrestFilterBuilder update(@NotNull Entry... entries) {
        if (entries.length == 0) throw new IllegalArgumentException("Cannot insert empty entry list");

        postgrestQuery.setHttpMethod(HttpMethod.PATCH);

        JsonObject jsonObject = new JsonObject();
        Arrays.stream(entries).forEach(entry -> jsonObject.addProperty(entry.getColumnName(), entry.getValue()));

        postgrestQuery.setBody(jsonObject);
        postgrestQuery.addHeader("Content-Type", "application/json");
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }

    public PostgrestFilterBuilder update(@NotNull JsonElement jsonElement) {
        postgrestQuery.setHttpMethod(HttpMethod.PATCH);
        postgrestQuery.setBody(jsonElement);
        postgrestQuery.addHeader("Content-Type", "application/json");
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }

    //TODO: Implement upsert
//    public PostgrestFilterBuilder upsert(boolean mergeDuplicates, @NotNull Entry... entries){
//        if (entries.length == 0) {
//            throw new IllegalArgumentException("Cannot insert empty entry list");
//        }
//
//        postgrestQuery.setHttpMethod(HttpMethod.POST);
//
//        JsonObject jsonObject = new JsonObject();
//        Arrays.stream(entries).forEach(entry -> jsonObject.addProperty(entry.getColumnName(), entry.getValue()));
//
//        postgrestQuery.setBody(jsonObject);
//        postgrestQuery.addHeader("Prefer", "resolution=" + (mergeDuplicates ? "merge-duplicates" :
//        "ignore-duplicates"));
//        postgrestQuery.addHeader("Content-Type", "application/json");
//        return new PostgrestFilterBuilder(this.postgrestQuery);
//    }

    public PostgrestFilterBuilder delete() {
        postgrestQuery.setHttpMethod(HttpMethod.DELETE);
        return new PostgrestFilterBuilder(this.postgrestQuery);
    }
}
