package com.assignment4.edit;

/**
 * Common interface for Edit Distance algorithms.
 * Allows benchmarking all implementations in a single loop.
 */
public interface EditDistance {

    /**
     * Computes the Levenshtein edit distance between strings a and b.
     * Allowed operations: insertion (cost 1), deletion (cost 1), substitution (cost 1).
     *
     * @param a the first string
     * @param b the second string
     * @return the minimum number of edit operations
     */
    int distance(String a, String b);

    /**
     * Returns the number of internal operations performed during computation.
     *
     * @return operation count
     */
    long getOperationsCount();

    /**
     * Resets the operation counter to 0.
     */
    void resetOperationsCount();
}
