package com.assignment4.search;

import java.util.List;

public interface StringSearch {

    List<Integer> search(String text, String pattern);

    long getComparisonsCount();

    void resetComparisonsCount();
}
