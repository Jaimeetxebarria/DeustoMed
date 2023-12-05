package org.deustomed.postgrest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.deustomed.httputils.HttpMethod;
import org.deustomed.httputils.UrlBuilder;
import org.junit.jupiter.api.Test;

import static org.deustomed.postgrest.PostgrestAssertions.assertJsonEquals;
import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.*;

class PostgrestQueryBuilderTest {

    PostgrestQueryBuilder getBlankPostgrestQueryBuilder() {
        PostgrestQuery query = new PostgrestQuery();
        query.setUrlBuilder(new UrlBuilder().setPath("/table"));
        return new PostgrestQueryBuilder(query);
    }

    @Test
    void select() {
        //Building url
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


        //Other parameters
        PostgrestQuery postgrestQuery = getBlankPostgrestQueryBuilder().select().getQuery();
        assertEquals(HttpMethod.GET, postgrestQuery.getHttpMethod());
    }

    @Test
    void insertSingleRow() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("column1", "value1");

        PostgrestTransformBuilder qb1 = getBlankPostgrestQueryBuilder().insert(jsonObject);
        assertPathnameEquals("/table", qb1.getQuery());

        String expectedJsonString1 = """
                {"column1":"value1"}""";
        assertJsonEquals(expectedJsonString1, qb1.getQuery().getBody());

        // 2 columns
        jsonObject.addProperty("column2", "value2");
        PostgrestFilterBuilder qb2 = getBlankPostgrestQueryBuilder().insert(jsonObject);
        assertPathnameEquals("/table", qb2.getQuery());

        String expectedJsonString2 = """
                {"column1":"value1","column2":"value2"}""";
        assertJsonEquals(expectedJsonString2, qb2.getQuery().getBody());

        //Other parameters
        PostgrestQuery postgrestQuery = qb2.getQuery();
        assertEquals(HttpMethod.POST, postgrestQuery.getHttpMethod());
        assertTrue(postgrestQuery.getHeader("Prefer").contains("missing=default"));
        assertTrue(postgrestQuery.getHeader("Content-Type").contains("application/json"));

        //Test defaultToNull
        PostgrestTransformBuilder qb3 = getBlankPostgrestQueryBuilder().insert(jsonObject, true);
        assertPathnameEquals("/table", qb3.getQuery());
        if (qb3.getQuery().getHeader("Prefer") != null) { //If null, then missing=default is not set anyway
            assertFalse(qb3.getQuery().getHeader("Prefer").contains("missing=default"));
        }
    }

    @Test
    void insertMultipleRows() {
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("column1", "value1");
        jsonObject1.addProperty("column2", "value2");
        jsonArray.add(jsonObject1);

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("column1", "value3");
        jsonObject2.addProperty("column2", "value4");
        jsonArray.add(jsonObject2);

        PostgrestFilterBuilder qb2 = getBlankPostgrestQueryBuilder().insert(jsonArray);
        assertPathnameEquals("/table", qb2.getQuery());

        String expectedJsonString = """
                [
                    {"column1":"value1","column2":"value2"},
                    {"column1":"value3","column2":"value4"}
                ]
                """;
        assertJsonEquals(expectedJsonString, qb2.getQuery().getBody());

        //Other parameters
        PostgrestQuery postgrestQuery = qb2.getQuery();
        assertEquals(HttpMethod.POST, postgrestQuery.getHttpMethod());
        assertTrue(postgrestQuery.getHeader("Prefer").contains("missing=default"));
        assertTrue(postgrestQuery.getHeader("Content-Type").contains("application/json"));

        //Test defaultToNull
        PostgrestFilterBuilder qb3 = getBlankPostgrestQueryBuilder().insert(jsonArray, true);
        assertPathnameEquals("/table", qb3.getQuery());
        assertJsonEquals(expectedJsonString, qb3.getQuery().getBody());
        if (qb3.getQuery().getHeader("Prefer") != null) { //If null, then missing=default is not set
            assertFalse(qb3.getQuery().getHeader("Prefer").contains("missing=default"));
        }
        assertTrue(postgrestQuery.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void updateWithJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("column1", "value1");

        PostgrestFilterBuilder qb1 = getBlankPostgrestQueryBuilder().update(jsonObject);
        assertPathnameEquals("/table", qb1.getQuery());

        String expectedJsonString1 = """
                {"column1":"value1"}""";
        assertJsonEquals(expectedJsonString1, qb1.getQuery().getBody());

        // 2 columns
        jsonObject.addProperty("column2", "value2");
        PostgrestFilterBuilder qb2 = getBlankPostgrestQueryBuilder().update(jsonObject);
        assertPathnameEquals("/table", qb2.getQuery());

        String expectedJsonString2 = """
                {"column1":"value1","column2":"value2"}""";
        assertJsonEquals(expectedJsonString2, qb2.getQuery().getBody());

        //Other parameters
        PostgrestQuery postgrestQuery = qb2.getQuery();
        assertEquals(HttpMethod.PATCH, postgrestQuery.getHttpMethod());
        assertTrue(postgrestQuery.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void updateWithEntries() {
        PostgrestFilterBuilder qb1 = getBlankPostgrestQueryBuilder()
                .update(new Entry("column1", "value1"));
        assertPathnameEquals("/table", qb1.getQuery());

        String expectedJsonString1 = """
                {"column1":"value1"}""";
        assertJsonEquals(expectedJsonString1, qb1.getQuery().getBody());

        // 2 columns
        PostgrestFilterBuilder qb2 = getBlankPostgrestQueryBuilder()
                .update(new Entry("column1", "value1"),
                        new Entry("column2", "value2"));
        assertPathnameEquals("/table", qb2.getQuery());

        String expectedJsonString2 = """
                {"column1":"value1","column2":"value2"}""";
        assertJsonEquals(expectedJsonString2, qb2.getQuery().getBody());

        //Other parameters
        PostgrestQuery postgrestQuery = qb2.getQuery();
        assertEquals(HttpMethod.PATCH, postgrestQuery.getHttpMethod());
        assertTrue(postgrestQuery.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void delete() {
        PostgrestFilterBuilder qb = getBlankPostgrestQueryBuilder().delete();
        assertPathnameEquals("/table", qb.getQuery());

        //Other parameters
        assertEquals(HttpMethod.DELETE, qb.getQuery().getHttpMethod());
    }

}