public class Main {

    private final static double[] A1 = {15, 13, 12, 13, 17};
    private final static double[] A2 = {16, 20, 12, 20, 12};

    public static void main(String[] args) {
        System.out.println("--- Статистика активів ---");
        double m1 = mean(A1);
        double m2 = mean(A2);
        double v1 = variance(A1, m1);
        double v2 = variance(A2, m2);
        double cov = covariance(A1, A2, m1, m2);
        double std1 = Math.sqrt(v1);
        double std2 = Math.sqrt(v2);

        double correlation = cov / (std1 * std2);

        System.out.printf("Акція A1: E(r) = %.2f%%, Var = %.2f, StdDev = %.2f%%\n", m1, v1, std1);
        System.out.printf("Акція A2: E(r) = %.2f%%, Var = %.2f, StdDev = %.2f%%\n", m2, v2, std2);
        System.out.printf("Коваріація: %.2f\n", cov);
        System.out.printf("Кореляція: %.4f\n", correlation);
        System.out.println();

        System.out.println("--- а) Портфель мінімального ризику ---");
        double w1_mvp = (v2 - cov) / (v1 + v2 - 2 * cov);
        printPortfolioStats("MVP", w1_mvp, m1, m2, v1, v2, cov);

        System.out.println("--- б) Ринковий портфель ---");
        double rf = 10.0;

        double num = (m1 - rf) * v2 - (m2 - rf) * cov;
        double den = (m1 - rf) * v2 + (m2 - rf) * v1 - (m1 + m2 - 2 * rf) * cov;
        double w1_market = num / den;

        printPortfolioStats("Ринковий", w1_market, m1, m2, v1, v2, cov);

        System.out.println("--- в) Портфель з очікуваним прибутком 15.25% ---");
        double targetReturn1 = 15.25;

        double w1_target1 = (targetReturn1 - m2) / (m1 - m2);
        printPortfolioStats("Target 15.25%", w1_target1, m1, m2, v1, v2, cov);

        System.out.println("--- г) Портфель з очікуваним прибутком 3.5% ---");
        double targetReturn2 = 3.5;
        double w1_target2 = (targetReturn2 - m2) / (m1 - m2);
        printPortfolioStats("Target 3.5%", w1_target2, m1, m2, v1, v2, cov);
    }

    public static void printPortfolioStats(String label, double w1, double m1, double m2, double v1, double v2, double cov) {
        double w2 = 1.0 - w1;

        double expReturn = w1 * m1 + w2 * m2;
        double portVar = (w1 * w1 * v1) + (w2 * w2 * v2) + (2 * w1 * w2 * cov);
        double portRisk = Math.sqrt(portVar);

        System.out.println("Структура портфеля (" + label + "):");
        System.out.printf("\tВага A1: %.4f (%.2f%%)\n", w1, w1 * 100);
        System.out.printf("\tВага A2: %.4f (%.2f%%)\n", w2, w2 * 100);
        System.out.printf("\tСподіваний прибуток (E): %.2f%%\n", expReturn);
        System.out.printf("\tРизик (StdDev): %.2f%%\n", portRisk);
    }

    public static double mean(double[] data) { // середнє арифметичне
        double sum = 0;
        for (double d : data) sum += d;
        return sum / data.length;
    }

    public static double variance(double[] data, double mean) { // дисперсія
        double sumSqDiff = 0;
        for (double d : data) sumSqDiff += Math.pow(d - mean, 2);
        return sumSqDiff / (data.length - 1);
    }

    public static double covariance(double[] a1, double[] a2, double m1, double m2) { // коваріація
        double sumProduct = 0;
        for (int i = 0; i < a1.length; i++) {
            sumProduct += (a1[i] - m1) * (a2[i] - m2);
        }
        return sumProduct / (a1.length - 1);
    }
}