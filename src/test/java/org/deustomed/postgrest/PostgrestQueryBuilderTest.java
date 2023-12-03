package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgrestQueryBuilderTest {

    @Test
    void from() {
        assertEquals("/table", new PostgrestQueryBuilder().from("table").getQuery().getUrlBuilder().getPathname());
        assertEquals("/refresh_token",
                new PostgrestQueryBuilder().from("refresh_token").getQuery().getUrlBuilder().getPathname());

        //Check that null errors out
        assertThrows(IllegalArgumentException.class,
                () -> new PostgrestQueryBuilder().from(null).getQuery().getUrlBuilder().getPathname());
        IllegalArgumentException exceptionBlank = assertThrows(IllegalArgumentException.class,
                () -> new PostgrestQueryBuilder().from("").getQuery().getUrlBuilder().getPathname());
        assertEquals("Cannot query blank table", exceptionBlank.getMessage());
    }

    @Test
    void select() {
        assertEquals("/table",
                new PostgrestQueryBuilder().from("table").select().getQuery().getUrlBuilder().getPathname());
        assertEquals("/table",
                new PostgrestQueryBuilder().from("table").select("*").getQuery().getUrlBuilder().getPathname());
        assertEquals("/table?select=column",
                new PostgrestQueryBuilder().from("table").select("column").getQuery().getUrlBuilder().getPathname());
        assertEquals("/table?select=column1,column2", new PostgrestQueryBuilder().from("table").select("column1",
                "column2").getQuery().getUrlBuilder().getPathname());

        assertThrows(IllegalArgumentException.class,
                () -> new PostgrestQueryBuilder().from("table").select(null).getQuery().getUrlBuilder().getPathname());
        assertThrows(IllegalArgumentException.class,
                () -> new PostgrestQueryBuilder().from("table").select("").getQuery().getUrlBuilder().getPathname());
        assertThrows(IllegalArgumentException.class, () -> new PostgrestQueryBuilder().from("table").select("column1"
                , null).getQuery().getUrlBuilder().getPathname());
        assertThrows(IllegalArgumentException.class, () -> new PostgrestQueryBuilder().from("table").select(null,
                "column2").getQuery().getUrlBuilder().getPathname());
        assertThrows(IllegalArgumentException.class, () -> new PostgrestQueryBuilder().from("table").select("column1"
                , null, "").getQuery().getUrlBuilder().getPathname());
        assertThrows(IllegalArgumentException.class, () -> new PostgrestQueryBuilder().from("table").select("column1"
                , null, "column2").getQuery().getUrlBuilder().getPathname());
    }

    @Test
    void insertSingleElement() {
        PostgrestQueryBuilder qb1 = new PostgrestQueryBuilder().from("table").insert(new Entry("column1", "value1"));
        assertEquals("/table", qb1.getQuery().getUrlBuilder().getPathname());
        assertEquals("{\"column1\":\"value1\"}", qb1.getQuery().getBody());

        PostgrestQueryBuilder qb2 = new PostgrestQueryBuilder().from("table").insert(new Entry("column1", "value1"),
                new Entry("column2", "value2"));
        assertEquals("/table", qb1.getQuery().getUrlBuilder().getPathname());
        assertEquals("{\"column1\":\"value1\"}", qb1.getQuery().getBody());
    }
}