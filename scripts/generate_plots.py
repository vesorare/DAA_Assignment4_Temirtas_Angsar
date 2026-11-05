import os
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

def create_plots():
    csv_path = os.path.join("results", "results.csv")
    plots_dir = os.path.join("results", "plots")
    os.makedirs(plots_dir, exist_ok=True)

    if not os.path.exists(csv_path):
        print(f"Error: {csv_path} not found.")
        return

    df = pd.read_csv(csv_path)

    # Style settings
    plt.rcParams.update({
        'font.size': 11,
        'axes.labelsize': 12,
        'axes.titlesize': 13,
        'xtick.labelsize': 10,
        'ytick.labelsize': 10,
        'legend.fontsize': 10,
        'figure.titlesize': 14,
        'figure.autolayout': True
    })

    # =========================================================================
    # PLOT 1: Edit Distance Operations on Log Scale (Recursive vs DP)
    # =========================================================================
    fig, ax = plt.subplots(figsize=(8, 5))
    df_ed_small = df[(df['family'] == 'EditDistance') & (df['n'] <= 14)]

    for alg in ['Recursive', 'Table', 'TwoRows']:
        for inp in ['random', 'worst_case_mismatch']:
            sub = df_ed_small[(df_ed_small['algorithm'] == alg) & (df_ed_small['input_type'] == inp)]
            if not sub.empty:
                marker = 'o' if 'Recursive' in alg else ('s' if 'Table' in alg else '^')
                linestyle = '-' if inp == 'random' else '--'
                label = f"{alg} ({inp})"
                ax.plot(sub['n'], sub['counter_value'], marker=marker, linestyle=linestyle, label=label, linewidth=1.8)

    ax.set_yscale('log')
    ax.set_title("Plot 1: Edit Distance Operations Count vs String Length (Log Scale)")
    ax.set_xlabel("String Length n (= m)")
    ax.set_ylabel("Operations Count (Log Scale)")
    ax.grid(True, which="both", ls="--", alpha=0.6)
    ax.legend(loc="upper left")
    plot1_path = os.path.join(plots_dir, "plot1_edit_distance_recursive_vs_dp_log.png")
    fig.savefig(plot1_path, dpi=300)
    plt.close(fig)
    print(f"Saved: {plot1_path}")

    # =========================================================================
    # PLOT 2: Edit Distance DP Scaling on Large N (Table vs TwoRows)
    # =========================================================================
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5.5))
    df_ed_large = df[(df['family'] == 'EditDistance') & (df['n'] >= 500)]

    # Subplot 2A: Execution Time vs N
    for alg in ['Table', 'TwoRows']:
        for inp in ['random', 'worst_case_mismatch']:
            sub = df_ed_large[(df_ed_large['algorithm'] == alg) & (df_ed_large['input_type'] == inp)]
            if not sub.empty:
                marker = 's' if alg == 'Table' else '^'
                linestyle = '-' if inp == 'random' else '--'
                ax1.plot(sub['n'], sub['time_ms'], marker=marker, linestyle=linestyle,
                         label=f"{alg} ({inp})", linewidth=1.8)

    ax1.set_title("Execution Time vs String Size N")
    ax1.set_xlabel("String Length n (= m)")
    ax1.set_ylabel("Wall-Clock Time (ms)")
    ax1.grid(True, ls="--", alpha=0.6)
    ax1.legend()

    # Subplot 2B: Cell Operations vs N^2 (Verification of O(n*m))
    for alg in ['Table', 'TwoRows']:
        sub = df_ed_large[(df_ed_large['algorithm'] == alg) & (df_ed_large['input_type'] == 'random')]
        if not sub.empty:
            ax2.plot(sub['n'], sub['counter_value'] / 1e6, marker='o', label=f"{alg} Cell Updates", linewidth=2)

    ax2.set_title("DP Table Cell Computations (Millions)")
    ax2.set_xlabel("String Length n (= m)")
    ax2.set_ylabel("Cell Computations (x 10^6)")
    ax2.grid(True, ls="--", alpha=0.6)
    ax2.legend()

    plot2_path = os.path.join(plots_dir, "plot2_edit_distance_dp_large_scaling.png")
    fig.suptitle("Plot 2: Scalability of Table vs Two-Rows DP on Large Inputs", fontsize=14)
    fig.savefig(plot2_path, dpi=300)
    plt.close(fig)
    print(f"Saved: {plot2_path}")

    # =========================================================================
    # PLOT 3: String Search Comparisons on Worst-Case Input (Ratio & Absolute)
    # =========================================================================
    fig, ax = plt.subplots(figsize=(8.5, 5.5))
    df_ss_worst = df[(df['family'] == 'StringSearch') & (df['input_type'] == 'worst_case_pathological')]

    naive_worst = df_ss_worst[df_ss_worst['algorithm'] == 'Naive']
    kmp_worst = df_ss_worst[df_ss_worst['algorithm'] == 'KMP']

    if not naive_worst.empty and not kmp_worst.empty:
        ax.plot(naive_worst['n'], naive_worst['counter_value'] / 1e6, 'ro-', label="Naive (O(n · m))", linewidth=2)
        ax.plot(kmp_worst['n'], kmp_worst['counter_value'] / 1e6, 'bs-', label="KMP (O(n + m))", linewidth=2)

        ax.set_title("Plot 3: Character Comparisons on Worst-Case Input (Pattern m = 250)")
        ax.set_xlabel("Text Length n")
        ax.set_ylabel("Comparisons (Millions)")
        ax.grid(True, ls="--", alpha=0.6)
        ax.legend(loc="upper left")

        # Annotate ratio at max size
        max_n = naive_worst['n'].max()
        n_val = naive_worst[naive_worst['n'] == max_n]['counter_value'].values[0]
        k_val = kmp_worst[kmp_worst['n'] == max_n]['counter_value'].values[0]
        ratio = n_val / k_val if k_val > 0 else 0
        ax.annotate(f"Ratio: {ratio:.1f}x fewer comparisons with KMP",
                    xy=(max_n, n_val / 1e6),
                    xytext=(max_n * 0.55, (n_val / 1e6) * 0.75),
                    arrowprops=dict(facecolor='black', shrink=0.05, width=1.2, headwidth=6))

    plot3_path = os.path.join(plots_dir, "plot3_string_search_comparisons_worst_case.png")
    fig.savefig(plot3_path, dpi=300)
    plt.close(fig)
    print(f"Saved: {plot3_path}")

    # =========================================================================
    # PLOT 4: String Search Wall-Clock Time (Random vs Pathological Worst-Case)
    # =========================================================================
    fig, ax = plt.subplots(figsize=(8.5, 5.5))
    df_ss = df[df['family'] == 'StringSearch']

    for alg, color in [('Naive', 'tab:red'), ('KMP', 'tab:blue')]:
        for inp, style, mark in [('random', ':', 'o'), ('worst_case_pathological', '-', 's')]:
            sub = df_ss[(df_ss['algorithm'] == alg) & (df_ss['input_type'] == inp)]
            if not sub.empty:
                label = f"{alg} ({'Random' if inp == 'random' else 'Worst-Case'})"
                ax.plot(sub['n'], sub['time_ms'], color=color, linestyle=style, marker=mark,
                        label=label, linewidth=2)

    ax.set_title("Plot 4: String Search Execution Time: Random vs Pathological Worst-Case")
    ax.set_xlabel("Text Length n")
    ax.set_ylabel("Wall-Clock Time (ms)")
    ax.grid(True, ls="--", alpha=0.6)
    ax.legend(loc="upper left")

    plot4_path = os.path.join(plots_dir, "plot4_string_search_time_random_vs_worst.png")
    fig.savefig(plot4_path, dpi=300)
    plt.close(fig)
    print(f"Saved: {plot4_path}")

if __name__ == '__main__':
    create_plots()
