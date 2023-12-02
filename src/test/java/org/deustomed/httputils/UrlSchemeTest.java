package org.deustomed.httputils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlSchemeTest {

    @Test
    void testToString() {
        assertEquals("http", UrlScheme.HTTP.toString());
        assertEquals("https", UrlScheme.HTTPS.toString());
    }

    @Test
    void testDefaultPort() {
        assertEquals(80, UrlScheme.HTTP.getDefaultPort());
        assertEquals(443, UrlScheme.HTTPS.getDefaultPort());
    }
}