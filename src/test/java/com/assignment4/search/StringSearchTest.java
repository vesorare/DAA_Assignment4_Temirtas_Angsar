package com.assignment4.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringSearchTest {

    private List<StringSearch> searchers;

    @BeforeEach
    void setUp() {
        searchers = List.of(
                new NaiveStringSearch(),
                new KmpStringSearch()
        );
    }

    @Test
    @DisplayName("Section 7 Reference: search('ABABDABACDABABCABAB', 'ABABCABAB') == [10]")
    void testReferenceSingleMatch() {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        for (StringSearch searcher : searchers) {
            assertEquals(List.of(10), searcher.search(text, pattern),
                    searcher.getClass().getSimpleName() + " failed on reference 1");
        }
    }

    @Test
    @DisplayName("Section 7 Reference: search('AAAAA', 'AA') == [0, 1, 2, 3] (overlapping)")
    void testReferenceOverlappingRepeated() {
        String text = "AAAAA";
        String pattern = "AA";
        for (StringSearch searcher : searchers) {
            assertEquals(List.of(0, 1, 2, 3), searcher.search(text, pattern),
                    searcher.getClass().getSimpleName() + " failed on overlapping AAAAA/AA");
        }
    }

    @Test
    @DisplayName("Section 7 Reference: search('ABABABA', 'ABA') == [0, 2, 4] (overlapping)")
    void testReferenceOverlappingPeriodic() {
        String text = "ABABABA";
        String pattern = "ABA";
        for (StringSearch searcher : searchers) {
            assertEquals(List.of(0, 2, 4), searcher.search(text, pattern),
                    searcher.getClass().getSimpleName() + " failed on overlapping ABABABA/ABA");
        }
    }

    @Test
    @DisplayName("Edge Case: Empty pattern throws IllegalArgumentException")
    void testEmptyPatternThrows() {
        for (StringSearch searcher : searchers) {
            assertThrows(IllegalArgumentException.class, () -> searcher.search("ANY TEXT", ""));
            assertThrows(IllegalArgumentException.class, () -> searcher.search("ANY TEXT", null));
        }
    }

    @Test
    @DisplayName("Edge Case: Pattern longer than text returns empty list")
    void testPatternLongerThanText() {
        for (StringSearch searcher : searchers) {
            assertTrue(searcher.search("SHORT", "LONGER_PATTERN").isEmpty(),
                    searcher.getClass().getSimpleName() + " failed on pattern longer than text");
        }
    }

    @Test
    @DisplayName("Edge Case: No match returns empty list")
    void testNoMatch() {
        for (StringSearch searcher : searchers) {
            assertTrue(searcher.search("HELLO WORLD", "XYZ").isEmpty(),
                    searcher.getClass().getSimpleName() + " failed on no match");
        }
    }

    @Test
    @DisplayName("Section 5: 100 random texts cross-validation (Naive and KMP return same list)")
    void testHundredRandomTextsAgreement() {
        Random rng = new Random(1337);
        String alphabet = "ABC"; // Small alphabet to ensure plenty of occurrences & overlaps

        NaiveStringSearch naive = new NaiveStringSearch();
        KmpStringSearch kmp = new KmpStringSearch();

        for (int test = 0; test < 100; test++) {
            int textLen = 50 + rng.nextInt(151); // 50..200
            int patLen = 1 + rng.nextInt(6);     // 1..6

            StringBuilder sbText = new StringBuilder();
            for (int i = 0; i < textLen; i++) {
                sbText.append(alphabet.charAt(rng.nextInt(alphabet.length())));
            }

            StringBuilder sbPat = new StringBuilder();
            for (int i = 0; i < patLen; i++) {
                sbPat.append(alphabet.charAt(rng.nextInt(alphabet.length())));
            }

            String text = sbText.toString();
            String pattern = sbPat.toString();

            List<Integer> naiveMatches = naive.search(text, pattern);
            List<Integer> kmpMatches = kmp.search(text, pattern);

            assertEquals(naiveMatches, kmpMatches,
                    "Mismatch for text='" + text + "', pattern='" + pattern + "'");
        }
    }
}
