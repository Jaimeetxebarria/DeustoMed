package org.deustomed.postgrest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.deustomed.postgrest.PostgrestAssertions.assertJsonEquals;
import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostgrestClientDatabaseTest {
    private static final PostgrestClient client = PostgrestClientFactory.createAnonymousClient();

    @Test
    @Order(1)
    void from() {
        assertPathnameEquals(client.getEndpoint() + "/table", client.from("table").getQuery());
        assertPathnameEquals(client.getEndpoint() + "/refresh_token", client.from("refresh_token").getQuery());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class,
                () -> client.from(null).getQuery().getUrlBuilder().getPathname());
        IllegalArgumentException exceptionBlank = assertThrows(IllegalArgumentException.class,
                () -> client.from("").getQuery().getUrlBuilder().getPathname());
        assertEquals("Cannot query blank table", exceptionBlank.getMessage());
    }

    @Test
    @Order(5)
    void selectQuery() {
        PostgrestQuery query1 = client
                .from("test_table")
                .select()
                .getQuery();

        String jsonResponse1 = """
                [
                    {"id": 1, "name": "Luis", "age": 20},
                    {"id": 2, "name": "José", "age": 22},
                    {"id": 3, "name": "María", "age": 25},
                    {"id": 4, "name": "Teresa", "age": 45}
                ]
                """;

        assertJsonEquals(jsonResponse1, client.sendQuery(query1));

        PostgrestQuery query2 = client
                .from("test_table")
                .select("name", "age")
                .gt("age", "20")
                .order("age", false)
                .limit(2)
                .offset(1)
                .getQuery();

        String jsonResponse2 = """
                [
                    {"name": "María", "age": 25},
                    {"name": "José", "age": 22}
                ]
                """;


        assertJsonEquals(jsonResponse2, client.sendQuery(query2));
    }

    @Test
    @Order(2)
    void insertSingleRowQuery() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 1);
        jsonObject.addProperty("name", "Luis");
        jsonObject.addProperty("age", 20);

        PostgrestQuery query = client
                .from("test_table")
                .insert(jsonObject)
                .select()
                .getQuery();

        String jsonResponse = """
                [
                    {"id": 1, "name": "Luis", "age": 20}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(query));
    }

    @Test
    @Order(4)
    void insertBulkQuery() {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("id", 1);
        jsonObject1.addProperty("name", "Luis");
        jsonObject1.addProperty("age", 20);

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("id", 2);
        jsonObject2.addProperty("name", "José");
        jsonObject2.addProperty("age", 22);

        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("id", 3);
        jsonObject3.addProperty("name", "María");
        jsonObject3.addProperty("age", 25);

        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("id", 4);
        jsonObject4.addProperty("name", "Teresa");
        jsonObject4.addProperty("age", 45);

        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);
        jsonArray.add(jsonObject4);

        PostgrestQuery query = client
                .from("test_table")
                .insert(jsonArray)
                .select()
                .getQuery();

        String jsonResponse = """
                [
                    {"id": 1, "name": "Luis", "age": 20},
                    {"id": 2, "name": "José", "age": 22},
                    {"id": 3, "name": "María", "age": 25},
                    {"id": 4, "name": "Teresa", "age": 45}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(query));
    }

    @Test
    @Order(6)
    void updateQuery() {
        PostgrestQuery query = client
                .from("test_table")
                .update(new Entry("name", "Alejandro"), new Entry("age", 19))
                .eq("id", "1")
                .select()
                .getQuery();

        String jsonResponse = """
                [
                    {"id": 1, "name": "Alejandro", "age": 19}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(query));
    }

    @Test
    @Order(7)
    void deleteAllQuery() {
        PostgrestQuery query = client
                .from("test_table")
                .delete()
                .not().is("id", null)
                .select()
                .getQuery();

        String jsonResponse = """
                [
                    {"id": 2, "name": "José", "age": 22},
                    {"id": 3, "name": "María", "age": 25},
                    {"id": 4, "name": "Teresa", "age": 45},
                    {"id": 1, "name": "Alejandro", "age": 19}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(query));
    }

    @Test
    @Order(3)
    void deleteSingleRowQuery() {
        PostgrestQuery query = client
                .from("test_table")
                .delete()
                .eq("id", "1")
                .select()
                .getQuery();

        String jsonResponse = """
                [
                    {"id": 1, "name": "Luis", "age": 20}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(query));
    }
}