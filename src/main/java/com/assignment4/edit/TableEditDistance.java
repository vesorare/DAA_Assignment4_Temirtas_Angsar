package com.assignment4.edit;

public class TableEditDistance implements EditDistance {

    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                operationsCount++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int deleteCost = dp[i - 1][j];
                    int insertCost = dp[i][j - 1];
                    int replaceCost = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }
        }

        return dp[n][m];
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
