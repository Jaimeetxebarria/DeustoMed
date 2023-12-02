package org.deustomed.httputils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UrlBuilderTest {

    @Test
    void testNullaryConstructor() {
        UrlBuilder urlBuilder = new UrlBuilder();
        assertEquals(UrlScheme.HTTP, urlBuilder.getUrlScheme());
        assertEquals("localhost", urlBuilder.getHostname());
        assertEquals(80, urlBuilder.getPort());
    }

    @Test
    void testBinaryConstructor() {
        UrlBuilder urlBuilder = new UrlBuilder(UrlScheme.HTTPS, "example.com");
        assertEquals(UrlScheme.HTTPS, urlBuilder.getUrlScheme());
        assertEquals("example.com", urlBuilder.getHostname());
        assertEquals(443, urlBuilder.getPort());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(null, "example.com"));
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(UrlScheme.HTTPS, null));
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(null, null));

        //Check that blank errors out
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(UrlScheme.HTTPS, ""));
    }

    @Test
    void testTernaryConstructor() {
        UrlBuilder urlBuilder = new UrlBuilder(UrlScheme.HTTPS, "example.com", 8080);
        assertEquals(UrlScheme.HTTPS, urlBuilder.getUrlScheme());
        assertEquals("example.com", urlBuilder.getHostname());
        assertEquals(8080, urlBuilder.getPort());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(null, "example.com", 8080));
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(UrlScheme.HTTPS, null, 8080));

        //Check that blank errors out
        assertThrows(IllegalArgumentException.class, () -> new UrlBuilder(UrlScheme.HTTPS, "", 8080));
    }

    @Test
    void getSetUrlScheme() {
        UrlBuilder urlBuilder = new UrlBuilder();
        assertEquals(UrlScheme.HTTP, urlBuilder.getUrlScheme());
        urlBuilder.setUrlScheme(UrlScheme.HTTPS);
        assertEquals(UrlScheme.HTTPS, urlBuilder.getUrlScheme());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setUrlScheme(null));
    }

    @Test
    void getSetHostname() {
        UrlBuilder urlBuilder = new UrlBuilder();
        assertEquals("localhost", urlBuilder.getHostname());
        urlBuilder.setHostname("www.example.com");
        assertEquals("www.example.com", urlBuilder.getHostname());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setHostname(null));

        //Check that blank errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setHostname(""));
    }

    @Test
    void getSetPort() {
        UrlBuilder urlBuilder = new UrlBuilder(UrlScheme.HTTP, "localhost");
        assertEquals(80, urlBuilder.getPort());
        urlBuilder.setPort(8080);
        assertEquals(8080, urlBuilder.getPort());

        //Check that negative errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setPort(-1));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setPort(-892348));

        //Check that port > 65535 errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setPort(65536));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setPort(9848912));
    }

    @Test
    void getSetQueryParameters() {
        UrlBuilder urlBuilder = new UrlBuilder();
        assertTrue(urlBuilder.getQueryParameters().isEmpty());

        urlBuilder.setQueryParameter("key1", "value1");
        assertEquals("value1", urlBuilder.getQueryParameter("key1"));

        //Overwrite parameter
        urlBuilder.setQueryParameter("key1", "value1overwritten");
        assertEquals("value1overwritten", urlBuilder.getQueryParameter("key1"));

        //Add another parameter
        urlBuilder.setQueryParameter("key2", "value2");
        assertEquals("value2", urlBuilder.getQueryParameter("key2"));

        //Check that parameters exist
        assertTrue(urlBuilder.hasQueryParameter("key1"));
        assertTrue(urlBuilder.hasQueryParameter("key2"));

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setQueryParameter(null, "value1"));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setQueryParameter("key1", null));

        //Check that blank errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setQueryParameter("", "value1"));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setQueryParameter("key1", ""));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.setQueryParameter("", ""));
    }

    @Test
    void addQueryParameter() {
        UrlBuilder urlBuilder = new UrlBuilder();
        assertTrue(urlBuilder.getQueryParameters().isEmpty());

        urlBuilder.addQueryParameter("key1", "value1");
        assertEquals("value1", urlBuilder.getQueryParameter("key1"));

        urlBuilder.addQueryParameter("key1", "value2");
        assertEquals("value1", urlBuilder.getQueryParameter("key1"));
        assertEquals("value2", urlBuilder.getQueryParameterList("key1").get(1));
        assertEquals(List.of("value1", "value2"), urlBuilder.getQueryParameterList("key1"));

        //Check that null errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.addQueryParameter(null, "value1"));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.addQueryParameter("key1", null));

        //Check that blank errors out
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.addQueryParameter("", "value1"));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.addQueryParameter("key1", ""));
        assertThrows(IllegalArgumentException.class, () -> urlBuilder.addQueryParameter("", ""));

    }

    @Test
    void build() {
        assertEquals("http://localhost", new UrlBuilder().toString());
        assertEquals("https://localhost", new UrlBuilder().setUrlScheme(UrlScheme.HTTPS).toString());
        assertEquals("http://example.com", new UrlBuilder().setHostname("example.com").toString());
        assertEquals("http://localhost:8080", new UrlBuilder().setPort(8080).toString());
        assertEquals("http://localhost/endpoint", new UrlBuilder().setPath("/endpoint").toString());
        assertEquals("http://localhost?query=value", new UrlBuilder().setQueryParameter("query", "value").toString());
        assertEquals("http://localhost?query=value&query2=value2", new UrlBuilder().setQueryParameter("query", "value")
                .setQueryParameter("query2", "value2").toString());

        assertEquals("https://database.com/rest/v1/users?select=name&age=eq.30",
                new UrlBuilder().setUrlScheme(UrlScheme.HTTPS).setHostname("database.com").setPath("/rest/v1/users").setQueryParameter("select", "name")
                        .setQueryParameter("age", "eq.30").toString());

        assertEquals("https://database.com/rest/v1/users?select=name&age=eq.30", new UrlBuilder(UrlScheme.HTTPS,
                "database.com", 443).setPath("/rest/v1/users").setQueryParameter("select", "name")
                .setQueryParameter("age", "eq.30").toString());

        assertEquals("http://database.com:8080/rest/v1/users?select=name&age=eq.30", new UrlBuilder(UrlScheme.HTTP,
                "database.com", 8080).setPath("/rest/v1/users").setQueryParameter("select", "name")
                .setQueryParameter("age", "eq.30").toString());

        //Multiple arguments
        assertEquals("https://database.com/rest/v1/users?select=name&age=gt.30&age=lt.40",
                new UrlBuilder(UrlScheme.HTTPS, "database.com").setPath("/rest/v1/users").setQueryParameter("select",
                                "name")
                        .addQueryParameter("age", "gt.30").addQueryParameter("age", "lt.40").toString());
    }
}