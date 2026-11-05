package com.assignment4.benchmark;

import java.util.Random;

/**
 * Deterministic data generator for Edit Distance and String Search benchmarks.
 * Uses a fixed seed to guarantee reproducible datasets.
 */
public class DataGenerator {

    private final Random random;

    public DataGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates a random string of length len over a given alphabet.
     */
    public String randomString(int len, String alphabet) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random string of lowercase Latin letters [a-z].
     */
    public String randomEnglishString(int len) {
        return randomString(len, "abcdefghijklmnopqrstuvwxyz");
    }

    /**
     * Generates a string of length len consisting entirely of character ch.
     */
    public String repeatedCharString(int len, char ch) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Generates worst-case string search input:
     * text = 'A' repeated n times
     * pattern = 'A' repeated (m - 1) times followed by 'B'
     */
    public String searchWorstCaseText(int n) {
        return repeatedCharString(n, 'A');
    }

    public String searchWorstCasePattern(int m) {
        if (m <= 1) return "B";
        return repeatedCharString(m - 1, 'A') + "B";
    }
}
