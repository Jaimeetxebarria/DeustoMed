package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.deustomed.postgrest.PostgrestAssertions.assertPathnameEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgrestFilterBuilderTest {
    @Test
    void eq() {
        assertPathnameEquals("?column=eq.value", new PostgrestFilterBuilder()
                .eq("column", "value").getQuery());

        assertPathnameEquals("?column=eq.value1&column=eq.value2", new PostgrestFilterBuilder()
                .eq("column", "value1")
                .eq("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().eq("", "value"));
    }

    @Test
    void neq() {
        assertPathnameEquals("?column=neq.value", new PostgrestFilterBuilder()
                .neq("column", "value").getQuery());

        assertPathnameEquals("?column=neq.value1&column=neq.value2", new PostgrestFilterBuilder()
                .neq("column", "value1")
                .neq("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().neq("", "value"));
    }

    @Test
    void lt() {
        assertPathnameEquals("?column=lt.value", new PostgrestFilterBuilder()
                .lt("column", "value").getQuery());

        assertPathnameEquals("?column=lt.value1&column=lt.value2", new PostgrestFilterBuilder()
                .lt("column", "value1")
                .lt("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lt("", "value"));
    }

    @Test
    void gt() {
        assertPathnameEquals("?column=gt.value", new PostgrestFilterBuilder()
                .gt("column", "value").getQuery());

        assertPathnameEquals("?column=gt.value1&column=gt.value2", new PostgrestFilterBuilder()
                .gt("column", "value1")
                .gt("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gt("", "value"));
    }

    @Test
    void gte() {
        assertPathnameEquals("?column=gte.value", new PostgrestFilterBuilder()
                .gte("column", "value").getQuery());

        assertPathnameEquals("?column=gte.value1&column=gte.value2", new PostgrestFilterBuilder()
                .gte("column", "value1")
                .gte("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().gte("", "value"));
    }

    @Test
    void lte() {
        assertPathnameEquals("?column=lte.value", new PostgrestFilterBuilder()
                .lte("column", "value").getQuery());

        assertPathnameEquals("?column=lte.value1&column=lte.value2", new PostgrestFilterBuilder()
                .lte("column", "value1")
                .lte("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().lte("", "value"));
    }

    @Test
    void like() {
        assertPathnameEquals("?column=like.value", new PostgrestFilterBuilder()
                .like("column", "value").getQuery());

        assertPathnameEquals("?column=like.value1&column=like.value2", new PostgrestFilterBuilder()
                .like("column", "value1")
                .like("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().like("", "value"));
    }

    @Test
    void ilike() {
        assertPathnameEquals("?column=ilike.value", new PostgrestFilterBuilder()
                .ilike("column", "value").getQuery());

        assertPathnameEquals("?column=ilike.value1&column=ilike.value2", new PostgrestFilterBuilder()
                .ilike("column", "value1")
                .ilike("column", "value2").getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().ilike("", "value"));
    }

    @Test
    void is() {
        assertPathnameEquals("?column=is.null", new PostgrestFilterBuilder()
                .is("column", null).getQuery());

        assertPathnameEquals("?column1=is.true&column2=is.false", new PostgrestFilterBuilder()
                .is("column1", true)
                .is("column2", false).getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is(null, null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().is("", null));
    }

    @Test
    void in() {
        assertPathnameEquals("?column=in.(\"value\")", new PostgrestFilterBuilder()
                .in("column", "value").getQuery());

        assertPathnameEquals("?column=in.(\"value1\",\"value2\")", new PostgrestFilterBuilder()
                .in("column", "value1", "value2").getQuery());

        assertPathnameEquals("?column=in.(\"value1\",\"value2\")&column=in.(\"value3\",\"value4\",\"value5\")",
                new PostgrestFilterBuilder()
                        .in("column", "value1", "value2")
                        .in("column", "value3", "value4", "value5").getQuery());

        assertPathnameEquals("?column=in.(1,2)&column=in.(3)",
                new PostgrestFilterBuilder().in("column", 1, 2).in("column", 3).getQuery());


        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("", "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", "value", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", "value", ""));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().in("column", 1, null));
    }

    @Test
    void or() {
        assertPathnameEquals("?or=(column1.eq.value1,column2.eq.value2)", new PostgrestFilterBuilder()
                .or(2)
                .eq("column1", "value1")
                .eq("column2", "value2")
                .getQuery());

        assertPathnameEquals("?or=(column.eq.value,column2.eq.value,column3.is.null)", new PostgrestFilterBuilder()
                .or(3)
                .eq("column", "value")
                .eq("column2", "value")
                .is("column3", null).getQuery());

        assertPathnameEquals("?or=(column.eq.value,column2.neq.value)&or=(column3.not.is.null,column4.is.true)",
                new PostgrestFilterBuilder()
                        .or(2)
                        .eq("column", "value")
                        .neq("column2", "value")
                        .or(2)
                        .not().is("column3", null) // Check if this not is valid postgREST syntax
                        .is("column4", true).getQuery());

        //TODO: Not enough filters are added
        //assertThrows(MalformedQueryException.class, () -> new PostgrestFilterBuilder().or(2).eq("column", "value").getQuery());

        //n should be at least 2
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().or(1).eq("column", "value"));
    }

    @Test
    void and() {
        assertPathnameEquals("?and=(column1.eq.value1,column2.eq.value2)", new PostgrestFilterBuilder()
                .and(2)
                .eq("column1", "value1")
                .eq("column2", "value2")
                .getQuery());

        assertPathnameEquals("?and=(column.eq.value,column2.eq.value,column3.is.null)", new PostgrestFilterBuilder()
                .and(3)
                .eq("column", "value")
                .eq("column2", "value")
                .is("column3", null).getQuery());

        assertPathnameEquals("?and=(column.eq.value,column2.neq.value)&and=(column3.not.is.null,column4.is.true)",
                new PostgrestFilterBuilder()
                        .and(2)
                        .eq("column", "value")
                        .neq("column2", "value")
                        .and(2)
                        .not().is("column3", null) // Check if this not is valid postgREST syntax
                        .is("column4", true).getQuery());

        //TODO: Not enough filters are added
        //assertThrows(MalformedQueryException.class, () -> new PostgrestFilterBuilder().or(2).eq("column", "value").getQuery());

        //n should be at least 2
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().and(1).eq("column", "value"));
    }

    @Test
    void combinedLogicalOperators() {
        //NOTE: AND goes before OR in the query because all query parameters with the same key are saved in the same list
        //and then added to the query on a key basis
        assertPathnameEquals("""
                        ?and=(column1.eq.value1,column2.eq.value2)&and=(column5.eq.value5,column6.eq.value6)\
                        &or=(column3.eq.value3,column4.eq.value4)&not.or=(column7.eq.value7,column8.eq.value8)""",
                new PostgrestFilterBuilder()
                        .and(2)
                        .eq("column1", "value1")
                        .eq("column2", "value2")
                        .or(2)
                        .eq("column3", "value3")
                        .eq("column4", "value4")
                        .and(2)
                        .eq("column5", "value5")
                        .eq("column6", "value6")
                        .not().or(2)
                        .eq("column7", "value7")
                        .eq("column8", "value8").getQuery());
    }

    @Test
    void nestedLogicalOperators() {
        assertPathnameEquals("?or=(column1.eq.value1,and(column3.eq.value3,column4.eq.value4))",
                new PostgrestFilterBuilder()
                        .or(2)
                        .eq("column1", "value1")
                        .and(2)
                        .eq("column3", "value3")
                        .eq("column4", "value4").getQuery());

        assertPathnameEquals("""
                        ?and=(column1.eq.value1,column2.eq.value2)\
                        &or=(column3.eq.value3,column4.eq.value4,and(column5.eq.value5,column6.eq.value6))""",
                new PostgrestFilterBuilder()
                        .and(2)
                        .eq("column1", "value1")
                        .eq("column2", "value2")
                        .or(3)
                        .eq("column3", "value3")
                        .eq("column4", "value4")
                        .and(2)
                        .eq("column5", "value5")
                        .eq("column6", "value6").getQuery());

        assertPathnameEquals("""
                        ?or=(and(column1.lt.value1,column2.eq.value2,and(column3.not.gte.value3,column4.eq.value4)),\
                        not.and(column5.eq.value5,column6.like.%value6%),not.or(column7.neq.value7,column8.eq.value8),\
                        or(column9.in.("value9a","value9b","value9c"),column10.eq.value12,column11.is.null))""",
                new PostgrestFilterBuilder()
                        .or(4)
                        .and(3)
                        .lt("column1", "value1")
                        .eq("column2", "value2")
                        .and(2)
                        .not().gte("column3", "value3")
                        .eq("column4", "value4")
                        .not().and(2)
                        .eq("column5", "value5")
                        .like("column6", "%value6%")
                        .not().or(2)
                        .neq("column7", "value7")
                        .eq("column8", "value8")
                        .or(3)
                        .in("column9", "value9a", "value9b", "value9c")
                        .eq("column10", "value12")
                        .is("column11", null).getQuery());
    }

    @Test
    void not() {
        assertPathnameEquals("?column=not.eq.value", new PostgrestFilterBuilder()
                .not().eq("column", "value").getQuery());

        assertPathnameEquals("?column=not.is.true&column=not.in.(\"value1\",\"value2\")",
                new PostgrestFilterBuilder()
                        .not().is("column", true)
                        .not().in("column", "value1", "value2")
                        .getQuery());

        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().eq(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().lt("column", null));
        assertThrows(IllegalArgumentException.class, () -> new PostgrestFilterBuilder().not().eq("", "value"));
    }
}