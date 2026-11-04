package com.assignment4.search;

import java.util.List;

/**
 * Common interface for substring search algorithms.
 * Both Naive and KMP implementations adhere to this contract.
 */
public interface StringSearch {

    /**
     * Finds all start indices of pattern occurrences in text, in increasing order,
     * including overlapping matches.
     *
     * @param text    the source text to search in
     * @param pattern the pattern to search for
     * @return list of 0-based starting indices of matches
     * @throws IllegalArgumentException if pattern is null or empty
     */
    List<Integer> search(String text, String pattern);

    /**
     * Returns the total number of character comparisons performed.
     *
     * @return comparison count
     */
    long getComparisonsCount();

    /**
     * Resets the comparison counter to 0.
     */
    void resetComparisonsCount();
}
