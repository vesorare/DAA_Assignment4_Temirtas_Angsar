# DAA Assignment 4: Dynamic Programming & String Algorithms

City Search & Spell-Check algorithms implementation and empirical analysis for **Design and Analysis of Algorithms (DAA)**.

---

## Project Structure

```text
Assignment4/
├── pom.xml                               # Maven project configuration (Java 17, JUnit 5)
├── README.md                             # Project documentation and reproduction instructions
├── REPORT.md                             # Experimental report with Card, plots, and analysis
├── scripts/
│   └── generate_plots.py                 # Python script to generate analytical charts
├── results/
│   ├── results.csv                       # Benchmark output data (time, operations, comparisons)
│   └── plots/
│       ├── plot1_edit_distance_recursive_vs_dp_log.png
│       ├── plot2_edit_distance_dp_large_scaling.png
│       ├── plot3_string_search_comparisons_worst_case.png
│       └── plot4_string_search_time_random_vs_worst.png
└── src/
    ├── main/java/com/assignment4/
    │   ├── edit/
    │   │   ├── EditDistance.java         # Common interface for edit distance algorithms
    │   │   ├── RecursiveEditDistance.java# Naive recursive Levenshtein (exponential O(3^n))
    │   │   ├── TableEditDistance.java    # Full 2D DP matrix table O(n*m) time and space
    │   │   └── TwoRowsEditDistance.java  # Space-optimized DP O(m) space
    │   ├── search/
    │   │   ├── StringSearch.java         # Common interface for string search
    │   │   ├── NaiveStringSearch.java    # Brute-force substring search
    │   │   └── KmpStringSearch.java      # Knuth-Morris-Pratt search with public lps()
    │   └── benchmark/
    │       ├── DataGenerator.java        # Reproducible random and pathological dataset generator
    │       └── BenchmarkRunner.java      # Automated benchmark harness exporting to CSV
    └── test/java/com/assignment4/
        ├── edit/
        │   └── EditDistanceTest.java     # Reference tests & 100 random short string agreements
        └── search/
            ├── LpsTest.java              # Reference LPS table assertions
            └── StringSearchTest.java     # Overlapping matches, edge cases & 100 cross-checks
```

---

## How to Build and Run

### Prerequisites
* **Java Development Kit (JDK 17+)**
* **Apache Maven 3.8+**
* **Python 3.8+** with `pandas` and `matplotlib` (for generating charts)

### 1. Build and Run Tests
Execute all unit tests (reference checks, 100 cross-validation randomized tests, edge cases):
```bash
mvn clean test
```

### 2. Run the Benchmark Experiment
To execute the automated benchmarks and generate `results/results.csv`:
```bash
mvn compile
java -cp target/classes com.assignment4.benchmark.BenchmarkRunner
```

### 3. Generate Analytical Plots
To reproduce the PNG charts in `results/plots/`:
```bash
python scripts/generate_plots.py
```

---

## AI-Use Acknowledgement

In accordance with academic integrity guidelines, AI assistance (Antigravity / Gemini) was used during this assignment to:
* Assist in structuring the Maven project configuration and JUnit 5 test fixtures;
* Automate boilerplate generation for the benchmark experiment runner;
* Formulate `matplotlib` plotting scripts to visualize CSV metrics with log axes;
* Proofread and format markdown report tables and documentation.

All core dynamic programming recurrences, state transitions, Knuth-Morris-Pratt prefix function implementations, edge-case handlers, and analytical conclusions were verified and validated against the course lecture notes.
