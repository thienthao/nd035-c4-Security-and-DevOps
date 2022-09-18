package com.udacity.examples.Testing;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelperTest {

    private static List<String> listStrings;
    private static List<Integer> listYears;

    @Before
    public void init() {
        listStrings = Arrays.asList("", "", "Hello", "World!");
        listYears = Arrays.asList(1990, 1991, 1992, 1993, 1994);
    }

    @Test
    public void test_getCount() {
        assertEquals(Helper.getCount(listStrings), 2);
    }

    @Test
    public void test_getStats() {
        IntSummaryStatistics statistics = Helper.getStats(listYears);
        assertEquals(statistics.getSum(), 9960);
        assertEquals(statistics.getCount(), 5);
        assertEquals(statistics.getMin(), 1990);
        assertEquals(statistics.getMax(), 1994);
        assertEquals(statistics.getAverage(), 1992, 0);
    }

    @Test
    public void test_getStringsOfLength3() {
        assertEquals(Helper.getStringsOfLength3(listStrings), 0);
    }

    @Test
    public void test_getSquareList() {
        assertEquals(Helper.getSquareList(Collections.singletonList(5)), Collections.singletonList(25));
    }

    @Test
    public void test_getMergedList() {
        assertEquals(Helper.getMergedList(listStrings), "Hello, World!");
    }

    @Test
    public void test_getFilteredList() {
        assertEquals(Helper.getFilteredList(listStrings), Arrays.asList("Hello", "World!"));
    }
}
