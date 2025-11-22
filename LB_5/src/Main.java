import java.util.Arrays;

public class Main {

    private static final double[][] PAY_OF_MATRIX = {
            {2.5, 3.5, 4.0},
            {1.5, 2.0, 3.5},
            {3.5, 8.0, 2.5},
            {7.5, 1.5, 3.5},
            {8.5, 1.5, 4.0}
    };

    private static final int ROWS = PAY_OF_MATRIX.length;
    private static final int COLS = PAY_OF_MATRIX[0].length;

    public static void main(String[] args) {
        System.out.println("--- Вихідна матриця прибутків ---");
        printMatrix(PAY_OF_MATRIX);

        // знаходимо максимуми
        double[] maxInColumns = new double[COLS];
        for (int j = 0; j < COLS; j++) {
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < ROWS; i++) {
                if (PAY_OF_MATRIX[i][j] > max) {
                    max = PAY_OF_MATRIX[i][j];
                }
            }
            maxInColumns[j] = max;
        }

        System.out.println("\nМаксимуми по стовпцях: " + Arrays.toString(maxInColumns));

        // знаходимо матрицю ризиків
        double[][] riskMatrix = new double[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                riskMatrix[i][j] = maxInColumns[j] - PAY_OF_MATRIX[i][j];
            }
        }

        System.out.println("\n--- Матриця ризиків (невикористаних можливостей) ---");
        printMatrix(riskMatrix);

        // знаходимо максимальний ризик для кожного варіанту
        double[] maxRisks = new double[ROWS];
        for (int i = 0; i < ROWS; i++) {
            double maxRisk = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < COLS; j++) {
                if (riskMatrix[i][j] > maxRisk) {
                    maxRisk = riskMatrix[i][j];
                }
            }
            maxRisks[i] = maxRisk;
        }

        System.out.println("\nМаксимальні ризики для кожного рішення:");
        for (int i = 0; i < ROWS; i++) {
            System.out.printf("x%d: %.2f%n", (i + 1), maxRisks[i]);
        }

        // 5. Обираємо мінімальний серед максимальних ризиків (Minimax)
        double minMaxRisk = Double.POSITIVE_INFINITY;
        int bestVariantIndex = -1;

        for (int i = 0; i < ROWS; i++) {
            if (maxRisks[i] < minMaxRisk) {
                minMaxRisk = maxRisks[i];
                bestVariantIndex = i;
            }
        }

        System.out.println("------------------------------------------------");
        System.out.printf("Оптимальне рішення за критерієм Севіджа: x%d%n", (bestVariantIndex + 1));
        System.out.printf("Мінімальний ризик: %.2f%n", minMaxRisk);
    }

    private static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("x" + (i + 1) + " | ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%6.2f ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}