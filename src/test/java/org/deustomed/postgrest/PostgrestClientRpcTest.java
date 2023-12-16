package org.deustomed.postgrest;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.deustomed.httputils.HttpMethod;
import org.junit.jupiter.api.Test;

import static org.deustomed.postgrest.PostgrestAssertions.assertJsonEquals;
import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostgrestClientRpcTest {

    private static final PostgrestClient client = PostgrestClientFactory.createAnonymousClient();

    @Test
    public void rpcNoParameters() {
        PostgrestQuery pq1 = client.rpc("test_no_parameters").getQuery();
        assertPathnameEquals(client.getEndpoint() + "/rpc/test_no_parameters", pq1);
        assertEquals(HttpMethod.GET, pq1.getHttpMethod());
        assertNull(pq1.getBody());

        assertEquals(new JsonPrimitive("No parameters"), client.sendQuery(pq1));

        PostgrestQuery pq2 = client.rpc("test_no_parameters_table").getQuery();
        assertPathnameEquals(client.getEndpoint() + "/rpc/test_no_parameters_table", pq2);
        assertEquals(HttpMethod.GET, pq2.getHttpMethod());
        assertNull(pq2.getBody());

        String jsonResponse = """
                [
                    {"foo": 1, "bar": "asdf", "bool": true},
                    {"foo": 2, "bar": "ghjk", "bool": false}
                ]
                """;

        assertJsonEquals(jsonResponse, client.sendQuery(pq2));
    }

    @Test
    public void rpcWithParametersJson() {
        JsonObject body = new JsonObject();
        body.addProperty("a", 5);
        body.addProperty("b", 3);

        PostgrestQuery pq1 = client.rpc("test_parameters_table", body).getQuery();
        assertPathnameEquals(client.getEndpoint() + "/rpc/test_parameters_table", pq1);
        assertEquals(HttpMethod.POST, pq1.getHttpMethod());
        assertEquals(body, pq1.getBody());

        assertJsonEquals("{\"sum\":8,\"difference\":2}", client.sendQuery(pq1).getAsJsonArray().get(0));
    }

    @Test
    public void rpcWithParametersEntries() {
        PostgrestQuery pq1 = client.rpc("test_parameters_table",
                new Entry<>("a", 5),
                new Entry<>("b", 3)).getQuery();

        assertPathnameEquals(client.getEndpoint() + "/rpc/test_parameters_table", pq1);
        assertEquals(HttpMethod.POST, pq1.getHttpMethod());
        assertJsonEquals("{\"a\":5,\"b\":3}", pq1.getBody());

        assertJsonEquals("{\"sum\":8,\"difference\":2}", client.sendQuery(pq1).getAsJsonArray().get(0));
    }
}
