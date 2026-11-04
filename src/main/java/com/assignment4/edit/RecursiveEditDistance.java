package com.assignment4.edit;

/**
 * DP Design (Recursive):
 * State: rec(i, j) = minimum edit distance between prefix a[0..i-1] and prefix b[0..j-1].
 * Base cases: rec(i, 0) = i (cost of deleting i chars), rec(0, j) = j (cost of inserting j chars).
 * Transition: rec(i, j) = if (a[i-1] == b[j-1]) rec(i-1, j-1)
 *             else 1 + min(rec(i-1, j), rec(i, j-1), rec(i-1, j-1)).
 */
public class RecursiveEditDistance implements EditDistance {

    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";
        return recursiveHelper(a, b, a.length(), b.length());
    }

    private int recursiveHelper(String a, String b, int i, int j) {
        operationsCount++;

        // Base cases: if one prefix is empty, return the length of the other
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

        // Transition:
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return recursiveHelper(a, b, i - 1, j - 1);
        }

        int deleteCost = recursiveHelper(a, b, i - 1, j);
        int insertCost = recursiveHelper(a, b, i, j - 1);
        int replaceCost = recursiveHelper(a, b, i - 1, j - 1);

        return 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
    }

    @Override
    public long getOperationsCount() {
        return operationsCount;
    }

    @Override
    public void resetOperationsCount() {
        operationsCount = 0;
    }
}
