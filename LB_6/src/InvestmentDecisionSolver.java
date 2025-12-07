import java.util.Arrays;

public class InvestmentDecisionSolver {

    public static void main(String[] args) {
        double[][] matrix = {
                {8, 2, 4},
                {6, 7, 4},
                {4, 7, 5},
                {3, 5, 6}
        };

        double[] probes = {0.5, 0.3, 0.2};
        double wIncome = 2.0 / 5.0;
        double wRisk = 3.0 / 5.0;

        System.out.println("--- Параметри задачі ---");
        System.out.printf("Вага критерію 'Макс. Дохід': %.2f%n", wIncome);
        System.out.printf("Вага критерію 'Мін. Ризик':  %.2f%n", wRisk);
        System.out.println("Ймовірності станів: " + Arrays.toString(probes));
        System.out.println("------------------------\n");

        int numProjects = matrix.length;
        double[] expectedValues = new double[numProjects];
        double[] variances = new double[numProjects];

        for (int i = 0; i < numProjects; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                expectedValues[i] += matrix[i][j] * probes[j];
            }

            for (int j = 0; j < matrix[i].length; j++) {
                variances[i] += Math.pow(matrix[i][j] - expectedValues[i], 2) * probes[j];
            }
        }

        double[] normIncome = normalize(expectedValues, true);
        double[] normRisk = normalize(variances, false);

        double[] finalScores = new double[numProjects];
        int bestProjectIndex = -1;
        double maxScore = -Double.MAX_VALUE;

        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s%n",
                "Проект", "E(x)", "Var(x)", "Norm(E)", "Norm(V)", "SCORE");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < numProjects; i++) {
            finalScores[i] = (wIncome * normIncome[i]) + (wRisk * normRisk[i]);

            System.out.printf("Proj %d     %-10.2f %-10.2f %-10.2f %-10.2f %-10.4f%n",
                    (i + 1), expectedValues[i], variances[i], normIncome[i], normRisk[i], finalScores[i]);

            if (finalScores[i] > maxScore) {
                maxScore = finalScores[i];
                bestProjectIndex = i;
            }
        }

        System.out.println("\n------------------------");
        System.out.printf("НАЙКРАЩЕ РІШЕННЯ: Проект %d з оцінкою %.4f%n",
                (bestProjectIndex + 1), maxScore);
    }

    /**
     * Метод нормалізації вектора значень.
     * @param data Масив даних
     * @param maximize Якщо true - нормалізуємо як "більше краще".
     * Якщо false - нормалізуємо як "менше краще" (для ризику).
     * @return Нормалізований масив [0..1]
     */
    public static double[] normalize(double[] data, boolean maximize) {
        double min = Arrays.stream(data).min().orElse(0);
        double max = Arrays.stream(data).max().orElse(0);
        double[] result = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            if (max == min) {
                result[i] = 1.0;
            } else {
                if (maximize) {
                    result[i] = (data[i] - min) / (max - min);
                } else {
                    result[i] = (max - data[i]) / (max - min);
                }
            }
        }
        return result;
    }
}