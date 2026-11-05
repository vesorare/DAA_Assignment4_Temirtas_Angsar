package com.assignment4.edit;

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

        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

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
