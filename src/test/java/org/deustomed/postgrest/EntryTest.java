package org.deustomed.postgrest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    @Test
    void testEqualsString() {
        assertEquals(new Entry("a", "b"), new Entry("a", "b")); //Same column names and values
        assertNotEquals(new Entry("a", "b"), new Entry("a", "c")); //Same column names, different values
        assertNotEquals(new Entry("a", "b"), new Entry("b", "b")); //Different column names, same values
        assertNotEquals(new Entry("a", "b"), new Entry("b", "c")); //Different column names, different values
        //Test nulls
        assertThrows(IllegalArgumentException.class, () -> new Entry(null, "b")); //Null column name
        assertThrows(IllegalArgumentException.class, () -> new Entry("a", (String) null)); //Null value
        assertThrows(IllegalArgumentException.class, () -> new Entry(null, (String) null)); //Null column name and value
        //Test blank
        assertThrows(IllegalArgumentException.class, () -> new Entry("", "b")); //Blank column name
        assertEquals(new Entry("a", ""), new Entry("a", "")); //Blank value
        assertThrows(IllegalArgumentException.class, () -> new Entry("", "")); //Blank column name and value
    }

    @Test
    void testEqualsInteger() {
        assertEquals(new Entry("a", 1), new Entry("a", 1)); //Same column names and values
        assertNotEquals(new Entry("a", 31), new Entry("a", 2)); //Same column names, different values
        assertNotEquals(new Entry("a", 1), new Entry("b", 1)); //Different column names, same values
        assertNotEquals(new Entry("a", 2), new Entry("b", 123)); //Different column names, different values
        //Test nulls
        assertThrows(IllegalArgumentException.class, () -> new Entry(null, 1)); //Null column name
        assertThrows(IllegalArgumentException.class, () -> new Entry("a", (Integer) null)); //Null value
        assertThrows(IllegalArgumentException.class, () -> new Entry(null, (Integer) null)); //Null column name and
        // value
        //Test blank
        assertThrows(IllegalArgumentException.class, () -> new Entry("", 1)); //Blank column name
    }


    @Test
    void testToString() {
        assertEquals("Entry{columnName='a', value='b'}", new Entry("a", "b").toString());
        assertEquals("Entry{columnName='a', value='1'}", new Entry("a", 1).toString());
    }
}