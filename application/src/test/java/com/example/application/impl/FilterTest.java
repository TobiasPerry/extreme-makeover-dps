package com.example.application.impl;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {

    @Test
    void testSettersAndGetters() {
        Filter<String> filter = new Filter<>();
        filter.setEq("abc");
        filter.setNeq("xyz");
        filter.setIn(Arrays.asList("a", "b"));
        filter.setNin(Arrays.asList("x", "y"));
        filter.setContains("text");
        filter.setNotContains("notText");

        assertEquals("abc", filter.getEq());
        assertEquals("xyz", filter.getNeq());
        assertEquals(Arrays.asList("a", "b"), filter.getIn());
        assertEquals(Arrays.asList("x", "y"), filter.getNin());
        assertEquals("text", filter.getContains());
        assertEquals("notText", filter.getNotContains());
    }

    @Test
    void testCopyConstructor() {
        Filter<String> original = new Filter<>();
        original.setEq("eq");
        original.setNeq("neq");
        original.setIn(Arrays.asList("one", "two"));
        original.setNin(Arrays.asList("three"));
        original.setContains("con");
        original.setNotContains("notCon");

        Filter<String> copy = new Filter<>(original);

        assertEquals(original, copy);
        assertNotSame(original.getIn(), copy.getIn()); // deep copy
        assertNotSame(original.getNin(), copy.getNin());
    }

    @Test
    void testCopyMethod() {
        Filter<String> filter = new Filter<>();
        filter.setEq("123");

        Filter<String> copied = filter.copy();

        assertEquals(filter, copied);
        assertNotSame(filter, copied);
    }

    @Test
    void testEqualsAndHashCode() {
        Filter<Integer> f1 = new Filter<>();
        f1.setEq(5);
        f1.setNeq(10);
        f1.setIn(List.of(1, 2));
        f1.setNin(List.of(3, 4));
        f1.setContains(7);
        f1.setNotContains(9);

        Filter<Integer> f2 = new Filter<>();
        f2.setEq(5);
        f2.setNeq(10);
        f2.setIn(List.of(1, 2));
        f2.setNin(List.of(3, 4));
        f2.setContains(7);
        f2.setNotContains(9);

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void testToStringIncludesSetFields() {
        Filter<String> filter = new Filter<>();
        filter.setEq("alpha");
        filter.setIn(List.of("a", "b"));

        String result = filter.toString();

        assertTrue(result.contains("eq=alpha"));
        assertTrue(result.contains("in=[a, b]"));
        assertTrue(result.startsWith("Filter{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testToStringEmptyFilter() {
        Filter<String> filter = new Filter<>();
        String result = filter.toString();
        assertEquals("Filter{}", result);
    }
}
