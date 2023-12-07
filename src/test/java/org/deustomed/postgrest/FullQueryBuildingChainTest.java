package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullQueryBuildingChainTest {
    static final String HOSTNAME = "hppqxyzzghzomojqpddp.supabase.co";
    static final String ENDPOINT = "/rest/v1";
    static final PostgrestClient postgrestClient = new PostgrestClient(HOSTNAME, ENDPOINT, "");

    void assertQueryEquals(String pathname, PostgrestQuery query) {
        assertEquals("https://" + HOSTNAME + ENDPOINT + pathname, query.getUrlBuilder().toString());
    }

    @Test
    void fullQuery() {
        PostgrestQuery query = postgrestClient.from("table").select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .is("column3", null)
                .in("column4", "value1", "value2", "value3")
                .not().like("column5", "value__")
                .order("column1", true)
                .limit(10)
                .offset(5)
                .getQuery();

        assertQueryEquals("""
                /table?select=column1,column2&column1=eq.value1&column2=gt.3&column3=is.null&column4=in.\
                ("value1","value2","value3")&column5=not.like.value__&order=column1.asc&limit=10&offset=5""", query);
    }

    @Test
    void fullQueryBlankErrors() {
        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("")
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("", "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("", true));
    }

    @Test
    void fullQueryNullErrors() {
        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from(null)
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select(null, "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq(null, "value1")
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("value1", null)
                .gt("column2", "3")
                .order("column1", true));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order(null, true));
    }

    @Test
    void fullQueryNegativeErrors() {
        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true)
                .limit(-1));

        assertThrows(IllegalArgumentException.class, () -> postgrestClient.from("table")
                .select("column1", "column2")
                .eq("column1", "value1")
                .gt("column2", "3")
                .order("column1", true)
                .offset(-1));
    }
}
