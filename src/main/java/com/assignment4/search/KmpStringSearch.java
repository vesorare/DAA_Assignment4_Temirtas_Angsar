package com.assignment4.search;

import java.util.ArrayList;
import java.util.List;

public class KmpStringSearch implements StringSearch {

    private long comparisonsCount = 0;

    public static int[] lps(String p) {
        if (p == null || p.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty for LPS computation");
        }

        int m = p.length();
        int[] lps = new int[m];
        lps[0] = 0;
        int len = 0;
        int i = 1;

        while (i < m) {
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }

        List<Integer> matches = new ArrayList<>();
        if (text == null || pattern.length() > text.length()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();
        int[] lpsArray = lps(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {
            comparisonsCount++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    matches.add(i - j);
                    j = lpsArray[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lpsArray[j - 1];
                } else {
                    i++;
                }
            }
        }

        return matches;
    }

    @Override
    public long getComparisonsCount() {
        return comparisonsCount;
    }

    @Override
    public void resetComparisonsCount() {
        comparisonsCount = 0;
    }
}
