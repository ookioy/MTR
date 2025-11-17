public class Main {

    private final static double CAPITAL_A = 50f;
    private final static double CAPITAL_B1 = 0f;      // lose
    private final static double CAPITAL_B2 = 200f;    // win

    private final static double PROBABILITY_B1 = 0.5;
    private final static double PROBABILITY_B2 = 0.5;

    @FunctionalInterface
    interface UtilityFunction {
        double calculate(double x);
    }

    public static void main(String[] args) {
        UtilityFunction person1Func = (x) -> 1.3 * x;
        UtilityFunction person2Func = (x) -> 1.4 * Math.sqrt(x);

        System.out.println("-- Студент 1 (U(x) = 1.3 * x):");
        analyzeDecision(person1Func);
        System.out.println();
        System.out.println("-- Студент 2 (U(x) = 1.4 * sqrt(x)):");
        analyzeDecision(person2Func);
    }

    private static void analyzeDecision(UtilityFunction personFunc) {
        double utilityA = personFunc.calculate(CAPITAL_A);
        double utilityB1 = personFunc.calculate(CAPITAL_B1);
        double utilityB2 = personFunc.calculate(CAPITAL_B2);
        double expectedUtilityB = PROBABILITY_B1 * utilityB1 + PROBABILITY_B2 * utilityB2;

        System.out.printf("Корисність (Варіант А: Зберегти гроші): %.4f\n", utilityA);
        System.out.printf("Очікувана корисність (Варіант Б: Ставка):   %.4f\n", expectedUtilityB);

        double epsilon = 0.00001;
        if (expectedUtilityB > utilityA) {
            System.out.println(">> Рішення: Зробити ставку (Варіант Б).");
        } else if (Math.abs(expectedUtilityB - utilityA) < epsilon) {
            System.out.println(">> Рішення: Байдуже (корисність однакова).");
        } else {
            System.out.println(">> Рішення: Зберегти гроші (Варіант А).");
        }
    }
}