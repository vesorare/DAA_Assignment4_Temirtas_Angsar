package com.assignment4.edit;

/**
 * DP Design (Two rows):
 * State: prev[j] represents dp[i-1][j] and curr[j] represents dp[i][j] (matching prefixes a[0..i-1] and b[0..j-1]).
 * Base cases: prev[j] = j for all 0 <= j <= m (i = 0), and curr[0] = i at each row iteration.
 * Transition: curr[j] = if (a[i-1] == b[j-1]) prev[j-1]
 *             else 1 + min(prev[j], curr[j-1], prev[j-1]).
 */
public class TwoRowsEditDistance implements EditDistance {

    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();

        // Edge case: empty strings
        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // Base case: i = 0 (empty prefix of string a)
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        // Iterate through rows
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                operationsCount++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int deleteCost = prev[j];       // dp[i-1][j]
                    int insertCost = curr[j - 1];   // dp[i][j-1]
                    int replaceCost = prev[j - 1];  // dp[i-1][j-1]
                    curr[j] = 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }
            // Swap rows
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
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
