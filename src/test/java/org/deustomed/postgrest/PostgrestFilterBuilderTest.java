package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgrestFilterBuilderTest {
    private String getPathname(PostgrestFilterBuilder postgrestFilterBuilder) {
        return postgrestFilterBuilder.getQuery().getUrlBuilder().getPathname();
    }

    @Test
    void eq() {
        assertEquals("?column=eq.value", getPathname(new PostgrestFilterBuilder().eq("column", "value")));
        assertEquals("?column=eq.value1&column=eq.value2", getPathname(new PostgrestFilterBuilder()
                .eq("column", "value1").eq("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq("", "value"));
    }

    @Test
    void neq() {
        assertEquals("?column=neq.value", getPathname(new PostgrestFilterBuilder().neq("column", "value")));
        assertEquals("?column=neq.value1&column=neq.value2", getPathname(new PostgrestFilterBuilder()
                .neq("column", "value1").neq("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq("", "value"));
    }

    @Test
    void lt() {
        assertEquals("?column=lt.value", getPathname(new PostgrestFilterBuilder().lt("column", "value")));
        assertEquals("?column=lt.value1&column=lt.value2", getPathname(new PostgrestFilterBuilder()
                .lt("column", "value1").lt("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt("", "value"));
    }

    @Test
    void gt() {
        assertEquals("?column=gt.value", getPathname(new PostgrestFilterBuilder().gt("column", "value")));
        assertEquals("?column=gt.value1&column=gt.value2", getPathname(new PostgrestFilterBuilder()
                .gt("column", "value1").gt("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt("", "value"));
    }

    @Test
    void gte() {
        assertEquals("?column=gte.value", getPathname(new PostgrestFilterBuilder().gte("column", "value")));
        assertEquals("?column=gte.value1&column=gte.value2", getPathname(new PostgrestFilterBuilder()
                .gte("column", "value1").gte("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte("", "value"));
    }

    @Test
    void lte() {
        assertEquals("?column=lte.value", getPathname(new PostgrestFilterBuilder().lte("column", "value")));
        assertEquals("?column=lte.value1&column=lte.value2", getPathname(new PostgrestFilterBuilder()
                .lte("column", "value1").lte("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte("", "value"));
    }

    @Test
    void like() {
        assertEquals("?column=like.value", getPathname(new PostgrestFilterBuilder().like("column", "value")));
        assertEquals("?column=like.value1&column=like.value2", getPathname(new PostgrestFilterBuilder()
                .like("column", "value1").like("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like("", "value"));
    }

    @Test
    void ilike() {
        assertEquals("?column=ilike.value", getPathname(new PostgrestFilterBuilder().ilike("column", "value")));
        assertEquals("?column=ilike.value1&column=ilike.value2", getPathname(new PostgrestFilterBuilder()
                .ilike("column", "value1").ilike("column", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike("", "value"));
    }

    @Test
    void is() {
        assertEquals("?column=is.true", getPathname(new PostgrestFilterBuilder().is("column", "true")));
        assertEquals("?column=is.null", getPathname(new PostgrestFilterBuilder().is("column", null)));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is("", "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is("column", "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is("column", ""));
    }

    @Test
    void in() {
        assertEquals("?column=in.(\"value\")", getPathname(new PostgrestFilterBuilder().in("column", "value")));
        assertEquals("?column=in.(\"value1\",\"value2\")", getPathname(new PostgrestFilterBuilder()
                .in("column", "value1", "value2")));

        assertEquals("?column=in.(\"value1\",\"value2\")&column=in.(\"value3\",\"value4\",\"value5\")",
                getPathname(new PostgrestFilterBuilder().in("column", "value1", "value2")
                        .in("column", "value3", "value4", "value5")));

        assertEquals("?column=in.(1,2)&column=in.(3)",
                getPathname(new PostgrestFilterBuilder().in("column", 1, 2).in("column", 3)));


        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("", "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", "value", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", "value", ""));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", 1, null));
    }

    @Test
    void not() {
        assertEquals("?column=not.eq.value", getPathname(new PostgrestFilterBuilder().not().eq("column", "value")));

        assertEquals("?column=not.is.true&column=not.in.(\"value1\",\"value2\")",
                getPathname(new PostgrestFilterBuilder()
                .not().is("column", "true").not().in("column", "value1", "value2")));

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().eq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().lt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().eq("", "value"));
    }
}