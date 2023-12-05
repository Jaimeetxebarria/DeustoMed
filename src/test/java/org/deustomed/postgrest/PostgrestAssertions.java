package org.deustomed.postgrest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostgrestAssertions {
    static void assertPathnameEquals(String expectedPathname, PostgrestQuery query) {
        assertEquals(expectedPathname, query.getUrlBuilder().getPathname());
    }
}
