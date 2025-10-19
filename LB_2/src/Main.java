import java.util.List;
import java.util.Map;

public class Main {
    private static final List<Map<Integer, Double>> results = List.of(
            Map.of(
                    3000, 0.5,
                    2000, 0.5
            ),
            Map.of(
                    2510, 0.99,
                    1510, 0.01
            )
    );

    public static void main(String[] args) {

        double minRiskValueCV = Double.MAX_VALUE;
        int minRiskOptionIndexCV = -1;

        double minExpectedShortfall = Double.MAX_VALUE;
        int minShortfallOptionIndex = -1;

        for (int i = 0; i < results.size(); i++) {
            Map<Integer, Double> outcomes = results.get(i);
            System.out.printf("--- Аналіз Опції %d ---\n", (i + 1));

            // сподіване значення
            double expectedValue = 0;
            for (Map.Entry<Integer, Double> entry : outcomes.entrySet()) {
                expectedValue += entry.getKey() * entry.getValue();
            }
            System.out.printf("1. Сподіване значення (NPV): %.2f\n", expectedValue);

            // варіація
            double variance = 0;
            for (Map.Entry<Integer, Double> entry : outcomes.entrySet()) {
                variance += Math.pow(entry.getKey() - expectedValue, 2) * entry.getValue();
            }
            System.out.printf("2. Варіація: %.2f\n", variance);

            // середньоквадратичне відхилення
            double stdDeviation = Math.sqrt(variance);
            System.out.printf("3. Середньоквадратичне відхилення: %.2f\n", stdDeviation);

            // коефіцієнт варіації (CV)
            double coeffOfVariation = (expectedValue == 0) ? 0 : stdDeviation / Math.abs(expectedValue);
            System.out.printf("4. Коефіцієнт варіації: %.4f (%.2f%%)\n", coeffOfVariation, coeffOfVariation * 100);

            // семіквадратичне відхилення
            double semiVariance = 0;
            for (Map.Entry<Integer, Double> entry : outcomes.entrySet()) {
                if (entry.getKey() < expectedValue) {
                    semiVariance += Math.pow(entry.getKey() - expectedValue, 2) * entry.getValue();
                }
            }
            double semiDeviation = Math.sqrt(semiVariance);
            System.out.printf("5. Семіквадратичне відхилення: %.2f\n", semiDeviation);

            // коефіцієнт семіваріації
            double coeffOfSemiVariation = (expectedValue == 0) ? 0 : semiDeviation / Math.abs(expectedValue);
            System.out.printf("6. Коефіцієнт семіваріації: %.4f (%.2f%%)\n", coeffOfSemiVariation, coeffOfSemiVariation * 100);

            // ризик сподіваних збитків
            double expectedShortfall = 0;
            for (Map.Entry<Integer, Double> entry : outcomes.entrySet()) {
                if (entry.getKey() < expectedValue) {
                    expectedShortfall += (expectedValue - entry.getKey()) * entry.getValue();
                }
            }
            System.out.printf("7. Ризик сподіваних збитків (Сподіваний дефіцит): %.2f\n", expectedShortfall);
            System.out.println("-------------------------");

            if (expectedShortfall < minExpectedShortfall) {
                minExpectedShortfall = expectedShortfall;
                minShortfallOptionIndex = i;
            }

            if (coeffOfVariation < minRiskValueCV) {
                minRiskValueCV = coeffOfVariation;
                minRiskOptionIndexCV = i;
            }
        }

        System.out.println("\n=== ЗАГАЛЬНІ ВИСНОВКИ ===");

        System.out.printf("Варіант з найменшим значенням 'ризику сподіваних збитків' (дефіциту): Опція %d (Дефіцит: %.2f)\n",
                (minShortfallOptionIndex + 1), minExpectedShortfall);

        System.out.printf("Найменш ризикований варіант (за коефіцієнтом варіації): Опція %d (CV = %.2f%%)\n",
                (minRiskOptionIndexCV + 1), minRiskValueCV * 100);
    }
}