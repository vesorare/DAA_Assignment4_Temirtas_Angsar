package com.assignment4.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LpsTest {

    @Test
    @DisplayName("Section 7 Reference: lps('AAACAAAA') == [0, 1, 2, 0, 1, 2, 3, 3]")
    void testReferenceLps1() {
        int[] expected = {0, 1, 2, 0, 1, 2, 3, 3};
        int[] actual = KmpStringSearch.lps("AAACAAAA");
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("Section 7 Reference: lps('ABABCABAB') == [0, 0, 1, 2, 0, 1, 2, 3, 4]")
    void testReferenceLps2() {
        int[] expected = {0, 0, 1, 2, 0, 1, 2, 3, 4};
        int[] actual = KmpStringSearch.lps("ABABCABAB");
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("Single character pattern")
    void testSingleChar() {
        assertArrayEquals(new int[]{0}, KmpStringSearch.lps("A"));
    }

    @Test
    @DisplayName("All repeated characters")
    void testRepeatedChars() {
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, KmpStringSearch.lps("AAAAA"));
    }

    @Test
    @DisplayName("Empty or null pattern throws IllegalArgumentException")
    void testEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> KmpStringSearch.lps(""));
        assertThrows(IllegalArgumentException.class, () -> KmpStringSearch.lps(null));
    }
}
