package com.assignment4.edit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditDistanceTest {

    private List<EditDistance> implementations;

    @BeforeEach
    void setUp() {
        implementations = List.of(
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowsEditDistance()
        );
    }

    @Test
    @DisplayName("Section 7 Reference: distance('kitten', 'sitting') == 3")
    void testReferenceKittenSitting() {
        for (EditDistance ed : implementations) {
            assertEquals(3, ed.distance("kitten", "sitting"),
                    ed.getClass().getSimpleName() + " failed on kitten/sitting");
        }
    }

    @Test
    @DisplayName("Section 7 Reference: distance('flaw', 'lawn') == 2")
    void testReferenceFlawLawn() {
        for (EditDistance ed : implementations) {
            assertEquals(2, ed.distance("flaw", "lawn"),
                    ed.getClass().getSimpleName() + " failed on flaw/lawn");
        }
    }

    @Test
    @DisplayName("Section 7 Reference: distance('intention', 'execution') == 5")
    void testReferenceIntentionExecution() {
        for (EditDistance ed : implementations) {
            assertEquals(5, ed.distance("intention", "execution"),
                    ed.getClass().getSimpleName() + " failed on intention/execution");
        }
    }

    @Test
    @DisplayName("Section 7 Reference: distance('', 'abc') == 3 and distance('abc', 'abc') == 0")
    void testReferenceEmptyAndIdentical() {
        for (EditDistance ed : implementations) {
            assertEquals(3, ed.distance("", "abc"),
                    ed.getClass().getSimpleName() + " failed on empty string a");
            assertEquals(3, ed.distance("abc", ""),
                    ed.getClass().getSimpleName() + " failed on empty string b");
            assertEquals(0, ed.distance("abc", "abc"),
                    ed.getClass().getSimpleName() + " failed on identical strings");
            assertEquals(0, ed.distance("", ""),
                    ed.getClass().getSimpleName() + " failed on both empty strings");
        }
    }

    @Test
    @DisplayName("Section 5: 100 random short strings cross-validation (all 3 versions agree)")
    void testHundredRandomShortStringsAgreement() {
        Random rng = new Random(42);
        String chars = "abcdef";

        for (int test = 0; test < 100; test++) {
            int lenA = rng.nextInt(8); // lengths 0..7 to keep recursion instantaneous
            int lenB = rng.nextInt(8);

            StringBuilder sbA = new StringBuilder();
            for (int i = 0; i < lenA; i++) {
                sbA.append(chars.charAt(rng.nextInt(chars.length())));
            }

            StringBuilder sbB = new StringBuilder();
            for (int i = 0; i < lenB; i++) {
                sbB.append(chars.charAt(rng.nextInt(chars.length())));
            }

            String a = sbA.toString();
            String b = sbB.toString();

            int resRec = implementations.get(0).distance(a, b);
            int resTab = implementations.get(1).distance(a, b);
            int resTwo = implementations.get(2).distance(a, b);

            assertEquals(resTab, resRec, "Recursive mismatch on: a='" + a + "', b='" + b + "'");
            assertEquals(resTab, resTwo, "TwoRows mismatch on: a='" + a + "', b='" + b + "'");
        }
    }
}
