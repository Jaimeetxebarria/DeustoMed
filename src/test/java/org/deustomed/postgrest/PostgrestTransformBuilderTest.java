package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostgrestTransformBuilderTest {
    private String getPathname(PostgrestFilterBuilder postgrestFilterBuilder) {
        return postgrestFilterBuilder.getQuery().getUrlBuilder().getPathname();
    }

    @Test
    void select() {
        assertTrue(new PostgrestTransformBuilder().select().getQuery().getHeader("Prefer").contains("return" +
                "=representation"));

        PostgrestTransformBuilder singleColumnSelect = new PostgrestTransformBuilder().select("column1");
        assertTrue(singleColumnSelect.getQuery().getHeader("Prefer").contains("return=representation"));
        assertEquals("?select=column1", singleColumnSelect.getQuery().getUrlBuilder().getPathname());

        PostgrestTransformBuilder multipleColumnSelect = new PostgrestTransformBuilder().select("column1", "column2",
                "column3");
        assertTrue(multipleColumnSelect.getQuery().getHeader("Prefer").contains("return=representation"));
        assertEquals("?select=column1,column2,column3", multipleColumnSelect.getQuery().getUrlBuilder().getPathname());
    }

    @Test
    void order() {
        PostgrestTransformBuilder ascendingOrder = new PostgrestTransformBuilder().order("column", true);
        assertEquals("?order=column.asc", ascendingOrder.getQuery().getUrlBuilder().getPathname());

        PostgrestTransformBuilder descendingOrder = new PostgrestTransformBuilder().order("column", false);
        assertEquals("?order=column.desc", descendingOrder.getQuery().getUrlBuilder().getPathname());

        PostgrestTransformBuilder multipleOrder = new PostgrestTransformBuilder()
                .order("column1", true)
                .order("column2", false);
        assertEquals("?order=column1.asc,column2.desc", multipleOrder.getQuery().getUrlBuilder().getPathname());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().order(null, true));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().order("", true));
    }

    @Test
    void limit() {
        assertEquals("?limit=10", new PostgrestTransformBuilder().limit(10).getQuery().getUrlBuilder().getPathname());
        assertEquals("?limit=34", new PostgrestTransformBuilder().limit(34).getQuery().getUrlBuilder().getPathname());
        assertEquals("?limit=0", new PostgrestTransformBuilder().limit(0).getQuery().getUrlBuilder().getPathname());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().limit(-1));
    }

    @Test
    void offset() {
        assertEquals("?offset=10", new PostgrestTransformBuilder().offset(10).getQuery().getUrlBuilder().getPathname());
        assertEquals("?offset=34", new PostgrestTransformBuilder().offset(34).getQuery().getUrlBuilder().getPathname());
        assertEquals("?offset=0", new PostgrestTransformBuilder().offset(0).getQuery().getUrlBuilder().getPathname());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().offset(-1));
    }


}