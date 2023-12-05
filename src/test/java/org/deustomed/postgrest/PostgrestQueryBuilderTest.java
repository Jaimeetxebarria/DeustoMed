package org.deustomed.postgrest;

import com.google.gson.JsonObject;
import org.deustomed.httputils.UrlBuilder;
import org.junit.jupiter.api.Test;

import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgrestQueryBuilderTest {

    PostgrestQueryBuilder getBlankPostgrestQueryBuilder() {
        PostgrestQuery query = new PostgrestQuery();
        query.setUrlBuilder(new UrlBuilder().setPath("/table"));
        return new PostgrestQueryBuilder(query);
    }

    @Test
    void select() {
        assertPathnameEquals("/table", getBlankPostgrestQueryBuilder().select().getQuery());
        assertPathnameEquals("/table", getBlankPostgrestQueryBuilder().select("*").getQuery());
        assertPathnameEquals("/table?select=column",
                getBlankPostgrestQueryBuilder().select("column").getQuery());
        assertPathnameEquals("/table?select=column1,column2",
                getBlankPostgrestQueryBuilder().select("column1", "column2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select(null).getQuery());
        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select("").getQuery());
        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select("column1", null).getQuery());
        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select(null, "column2").getQuery());
        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select("column1", null, "").getQuery());
        assertThrows(IllegalArgumentException.class, () -> getBlankPostgrestQueryBuilder()
                .select("column1", null, "column2").getQuery());
    }

    @Test
    void insertSingleElement() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("column1", "value1");
        PostgrestTransformBuilder qb1 = getBlankPostgrestQueryBuilder()
                .insert(jsonObject);
        assertPathnameEquals("/table", qb1.getQuery());
        assertEquals("{\"column1\":\"value1\"}", qb1.getQuery().getBody().toString());

        jsonObject.addProperty("column2", "value2");
        PostgrestTransformBuilder qb2 = getBlankPostgrestQueryBuilder()
                .insert(jsonObject);
        assertPathnameEquals("/table", qb2.getQuery());
        assertEquals("{\"column1\":\"value1\",\"column2\":\"value2\"}", qb2.getQuery().getBody().toString());
    }


}