package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertPathnameEquals("?select=column1", singleColumnSelect.getQuery());

        PostgrestTransformBuilder multipleColumnSelect = new PostgrestTransformBuilder()
                .select("column1", "column2", "column3");
        assertTrue(multipleColumnSelect.getQuery().getHeader("Prefer").contains("return=representation"));
        assertPathnameEquals("?select=column1,column2,column3", multipleColumnSelect.getQuery());
    }

    @Test
    void order() {
        PostgrestTransformBuilder ascendingOrder = new PostgrestTransformBuilder().order("column", true);
        assertPathnameEquals("?order=column.asc", ascendingOrder.getQuery());

        PostgrestTransformBuilder descendingOrder = new PostgrestTransformBuilder().order("column", false);
        assertPathnameEquals("?order=column.desc", descendingOrder.getQuery());

        PostgrestTransformBuilder multipleOrder = new PostgrestTransformBuilder()
                .order("column1", true)
                .order("column2", false);
        assertPathnameEquals("?order=column1.asc,column2.desc", multipleOrder.getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().order(null, true));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().order("", true));
    }

    @Test
    void limit() {
        assertPathnameEquals("?limit=10", new PostgrestTransformBuilder().limit(10).getQuery());
        assertPathnameEquals("?limit=34", new PostgrestTransformBuilder().limit(34).getQuery());
        assertPathnameEquals("?limit=0", new PostgrestTransformBuilder().limit(0).getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().limit(-1));
    }

    @Test
    void offset() {
        assertPathnameEquals("?offset=10", new PostgrestTransformBuilder().offset(10).getQuery());
        assertPathnameEquals("?offset=34", new PostgrestTransformBuilder().offset(34).getQuery());
        assertPathnameEquals("?offset=0", new PostgrestTransformBuilder().offset(0).getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestTransformBuilder().offset(-1));
    }


}