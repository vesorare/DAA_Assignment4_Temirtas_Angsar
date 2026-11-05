package com.assignment4.edit;

public interface EditDistance {

    int distance(String a, String b);

    long getOperationsCount();

    void resetOperationsCount();
}
