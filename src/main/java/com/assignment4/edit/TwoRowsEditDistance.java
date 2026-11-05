package com.assignment4.edit;

public class TwoRowsEditDistance implements EditDistance {

    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        int n = a.length();
        int m = b.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                operationsCount++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int deleteCost = prev[j];
                    int insertCost = curr[j - 1];
                    int replaceCost = prev[j - 1];
                    curr[j] = 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }
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
