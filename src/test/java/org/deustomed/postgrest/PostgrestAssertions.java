package org.deustomed.postgrest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgrestAssertions {
    static void assertPathnameEquals(String expectedPathname, PostgrestQuery query) {
        assertEquals(expectedPathname, query.getUrlBuilder().getPathname());
    }

    static void assertJsonEquals(String expected, JsonElement actual) {
        assertEquals(JsonParser.parseString(expected), actual);
    }

    public static void assertDatabaseUserEquals(String expected, PostgrestClient postgrestClient) {
        PostgrestQuery query = postgrestClient.rpc("get_current_role").getQuery();
        assertJsonEquals(expected, postgrestClient.sendQuery(query));
    }
}
