package main.java;

import java.util.*;

public class DecisionHelper {

    // Possible states
    private static final String[] STATES = {"Дощ", "Сонце"};

    // Possible user actions
    private static final String[] ACTIONS = {"Сидіти вдома", "Піти в ліс"};

    // Map of feelings (text input -> numeric value)
    private static final Map<String, Integer> FEELINGS_MAP = new HashMap<>();
    static {
        FEELINGS_MAP.put("дуже добре", 10);
        FEELINGS_MAP.put("добре", 8);
        FEELINGS_MAP.put("посередньо", 6);
        FEELINGS_MAP.put("погано", 3);
        FEELINGS_MAP.put("дуже погано", 1);
    }

    /**
     * Main entry point of the program.
     * - Reads probabilities for each state.
     * - Reads user preferences (utilities) for each action in each state.
     * - Calculates expected values and finds the best action.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double[] probabilities = readProbabilities(scanner);
        double[][] utilities = readUtilities(scanner);

        scanner.close();

        String bestAction = calculateBestAction(probabilities, utilities);
        System.out.println("\nНайоптимальніший вибір: " + bestAction);
    }

    /**
     * Reads probabilities for each state from user input.
     * Ensures that:
     *  - Each probability is between 0 and 1.
     *  - The sum of probabilities equals 1.
     *
     * @param scanner Scanner for user input
     * @return array of probabilities for each state
     */
    private static double[] readProbabilities(Scanner scanner) {
        double[] probabilities = new double[STATES.length];

        while (true) {
            System.out.println("Введіть ймовірності для кожного стану (у сумі має бути 1):");
            double sum = 0;
            boolean valid = true;

            for (int i = 0; i < STATES.length; i++) {
                System.out.print("Ймовірність для '" + STATES[i] + "': ");
                try {
                    probabilities[i] = Double.parseDouble(scanner.nextLine().replace(",", "."));
                    if (probabilities[i] < 0 || probabilities[i] > 1) {
                        System.out.println("Ймовірність має бути від 0 до 1. Спробуйте ще раз.\n");
                        valid = false;
                        break;
                    }
                    sum += probabilities[i];
                } catch (NumberFormatException e) {
                    System.out.println("Будь ласка, введіть число.\n");
                    valid = false;
                    break;
                }
            }

            // Check if probabilities sum to 1
            if (sum == 1) {
                break;
            } 
            System.out.println("Сума ймовірностей має дорівнювати 1. Спробуйте ще раз.\n");
        }

        return probabilities;
    }

    /**
     * Reads user's subjective utilities for each combination of (action, state).
     * User must input predefined text,
     * which is mapped to a numeric score.
     *
     * @param scanner Scanner for user input
     * @return 2D array of utilities: [action][state]
     */
    private static double[][] readUtilities(Scanner scanner) {
        double[][] utilities = new double[ACTIONS.length][STATES.length];

        System.out.println("\nОцініть свою реакцію (пишіть словами):");
        System.out.println("Можливі варіанти: " + FEELINGS_MAP.keySet());

        for (int i = 0; i < ACTIONS.length; i++) {
            for (int j = 0; j < STATES.length; j++) {
                while (true) {
                    System.out.print("Якщо буде '" + STATES[j] + "' і ви вирішите '" + ACTIONS[i] + "': ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (FEELINGS_MAP.containsKey(input)) {
                        utilities[i][j] = FEELINGS_MAP.get(input);
                        break;
                    } else {
                        System.out.println("Невірний ввід. Спробуйте ще раз. Варіанти: " + FEELINGS_MAP.keySet());
                    }
                }
            }
        }
        return utilities;
    }

    /**
     * Calculates the expected value of each action and finds the best one.
     * Expected value = sum(probability(state) * utility(action, state))
     *
     * @param probabilities probabilities of each state
     * @param utilities utility matrix [action][state]
     * @return name of the best action
     */
    private static String calculateBestAction(double[] probabilities, double[][] utilities) {
        double bestValue = Double.NEGATIVE_INFINITY;
        String bestAction = "";

        for (int i = 0; i < ACTIONS.length; i++) {
            double expectedValue = 0;
            for (int j = 0; j < STATES.length; j++) {
                expectedValue += probabilities[j] * utilities[i][j];
            }
            System.out.printf("Очікувана цінність дії '%s': %.2f\n", ACTIONS[i], expectedValue);

            if (expectedValue > bestValue) {
                bestValue = expectedValue;
                bestAction = ACTIONS[i];
            }
        }
        return bestAction;
    }
}
