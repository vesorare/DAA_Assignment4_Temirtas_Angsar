package com.assignment4.benchmark;

import java.util.Random;

public class DataGenerator {

    private final Random random;

    public DataGenerator(long seed) {
        this.random = new Random(seed);
    }

    public String randomString(int len, String alphabet) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public String randomEnglishString(int len) {
        return randomString(len, "abcdefghijklmnopqrstuvwxyz");
    }

    public String repeatedCharString(int len, char ch) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public String searchWorstCaseText(int n) {
        return repeatedCharString(n, 'A');
    }

    public String searchWorstCasePattern(int m) {
        if (m <= 1) return "B";
        return repeatedCharString(m - 1, 'A') + "B";
    }
}
