package com.assignment4.search;

import java.util.ArrayList;
import java.util.List;

public class NaiveStringSearch implements StringSearch {

    private long comparisonsCount = 0;

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

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                comparisonsCount++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                matches.add(i);
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
