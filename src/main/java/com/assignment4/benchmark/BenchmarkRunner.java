package com.assignment4.benchmark;

import com.assignment4.edit.EditDistance;
import com.assignment4.edit.RecursiveEditDistance;
import com.assignment4.edit.TableEditDistance;
import com.assignment4.edit.TwoRowsEditDistance;
import com.assignment4.search.KmpStringSearch;
import com.assignment4.search.NaiveStringSearch;
import com.assignment4.search.StringSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BenchmarkRunner {

    private static final long SEED = 42L;
    private static final int REPETITIONS = 5;

    public static void main(String[] args) {
        System.out.println("Starting Benchmark Execution...");

        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }
        File plotsDir = new File("results/plots");
        if (!plotsDir.exists()) {
            plotsDir.mkdirs();
        }

        File csvFile = new File("results/results.csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("algorithm,family,input_type,n,m,time_ms,counter_name,counter_value");

            DataGenerator gen = new DataGenerator(SEED);

            warmup(gen);

            runEditDistanceSmall(writer, gen);

            runEditDistanceLarge(writer, gen);

            runStringSearch(writer, gen);

            System.out.println("Benchmark successfully finished. Data written to " + csvFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void warmup(DataGenerator gen) {
        System.out.println("Warming up JIT compiler...");
        TableEditDistance table = new TableEditDistance();
        NaiveStringSearch naive = new NaiveStringSearch();
        KmpStringSearch kmp = new KmpStringSearch();

        for (int i = 0; i < 50; i++) {
            String s1 = gen.randomEnglishString(100);
            String s2 = gen.randomEnglishString(100);
            table.distance(s1, s2);

            String t = gen.randomString(1000, "ABCD");
            String p = gen.randomString(20, "ABCD");
            naive.search(t, p);
            kmp.search(t, p);
        }
    }

    private static void runEditDistanceSmall(PrintWriter writer, DataGenerator gen) {
        System.out.println("Running Edit Distance (Small sizes, including Recursive)...");

        int[] sizes = {6, 8, 10, 12, 14};
        String[] inputTypes = {"random", "worst_case_mismatch"};

        for (int n : sizes) {
            for (String inputType : inputTypes) {
                String a;
                String b;
                if ("random".equals(inputType)) {
                    a = gen.randomEnglishString(n);
                    b = gen.randomEnglishString(n);
                } else {
                    a = gen.repeatedCharString(n, 'a');
                    b = gen.repeatedCharString(n, 'b');
                }

                benchmarkEditDistance(writer, new RecursiveEditDistance(), "Recursive", inputType, a, b, n);

                benchmarkEditDistance(writer, new TableEditDistance(), "Table", inputType, a, b, n);

                benchmarkEditDistance(writer, new TwoRowsEditDistance(), "TwoRows", inputType, a, b, n);
            }
        }
    }

    private static void runEditDistanceLarge(PrintWriter writer, DataGenerator gen) {
        System.out.println("Running Edit Distance (Large sizes: Table vs TwoRows)...");

        int[] sizes = {500, 1000, 2000, 4000};
        String[] inputTypes = {"random", "worst_case_mismatch"};

        for (int n : sizes) {
            for (String inputType : inputTypes) {
                String a;
                String b;
                if ("random".equals(inputType)) {
                    a = gen.randomEnglishString(n);
                    b = gen.randomEnglishString(n);
                } else {
                    a = gen.repeatedCharString(n, 'a');
                    b = gen.repeatedCharString(n, 'b');
                }

                benchmarkEditDistance(writer, new TableEditDistance(), "Table", inputType, a, b, n);

                benchmarkEditDistance(writer, new TwoRowsEditDistance(), "TwoRows", inputType, a, b, n);
            }
        }
    }

    private static void benchmarkEditDistance(PrintWriter writer, EditDistance alg, String name,
                                              String inputType, String a, String b, int n) {
        List<Double> times = new ArrayList<>();
        long operations = 0;

        for (int r = 0; r < REPETITIONS; r++) {
            alg.resetOperationsCount();
            long start = System.nanoTime();
            alg.distance(a, b);
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000.0);
            operations = alg.getOperationsCount();
        }

        Collections.sort(times);
        double medianTime = times.get(times.size() / 2);

        String row = String.format(Locale.US, "%s,EditDistance,%s,%d,%d,%.5f,operations_count,%d",
                name, inputType, n, n, medianTime, operations);
        writer.println(row);
        writer.flush();
    }

    private static void runStringSearch(PrintWriter writer, DataGenerator gen) {
        System.out.println("Running String Search (Naive vs KMP)...");

        int[] textSizes = {25000, 50000, 100000, 200000};
        int patternSize = 250;
        String[] inputTypes = {"random", "worst_case_pathological"};

        for (int n : textSizes) {
            for (String inputType : inputTypes) {
                String text;
                String pattern;

                if ("random".equals(inputType)) {
                    text = gen.randomString(n, "ABCD");
                    pattern = gen.randomString(patternSize, "ABCD");
                } else {
                    text = gen.searchWorstCaseText(n);
                    pattern = gen.searchWorstCasePattern(patternSize);
                }

                benchmarkStringSearch(writer, new NaiveStringSearch(), "Naive", inputType, text, pattern, n, patternSize);

                benchmarkStringSearch(writer, new KmpStringSearch(), "KMP", inputType, text, pattern, n, patternSize);
            }
        }
    }

    private static void benchmarkStringSearch(PrintWriter writer, StringSearch alg, String name,
                                              String inputType, String text, String pattern, int n, int m) {
        List<Double> times = new ArrayList<>();
        long comparisons = 0;

        for (int r = 0; r < REPETITIONS; r++) {
            alg.resetComparisonsCount();
            long start = System.nanoTime();
            alg.search(text, pattern);
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000.0);
            comparisons = alg.getComparisonsCount();
        }

        Collections.sort(times);
        double medianTime = times.get(times.size() / 2);

        String row = String.format(Locale.US, "%s,StringSearch,%s,%d,%d,%.5f,comparisons_count,%d",
                name, inputType, n, m, medianTime, comparisons);
        writer.println(row);
        writer.flush();
    }
}
