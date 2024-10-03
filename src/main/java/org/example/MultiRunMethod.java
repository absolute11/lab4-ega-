package org.example;

import java.util.*;

public class MultiRunMethod {

    // Константа для количества кодировок, выводимых на консоль
    private static final int DISPLAY_COUNT = 32;

    public static void main(String[] args) {

        int L = 7; // Длина кодировки остается 7 для 128 кодировок

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество шагов (N): ");
        int N = scanner.nextInt();

        System.out.print("Введите количество запусков (RUNS): ");
        int RUNS = scanner.nextInt();

        // Генерация пространства поиска и ландшафта приспособленности
        List<Integer> searchSpace = generateSearchSpace(L);
        Map<Integer, Double> fitnessMap = generateFitnessMap(searchSpace);

        // Выбор 32 уникальных случайных кодировок для вывода
        List<Integer> displayKeys = selectRandomKeys(fitnessMap.keySet(), DISPLAY_COUNT);

        // Вывод выбранных 32 кодировок с приспособленностью
        System.out.println("\nЛандшафт Приспособленности (32 из 128 кодировок):");
        System.out.println("-------------------------------------------------------");
        for (int key : displayKeys) {
            String binaryString = toBinaryString(key, L);
            System.out.println(binaryString + " (μ = " + String.format("%.2f", fitnessMap.get(key)) + ")");
        }
        System.out.println("-------------------------------------------------------\n");

        // Создание экземпляров алгоритмов
        MonteCarloMethod monteCarloMethod = new MonteCarloMethod(L, N, fitnessMap);
        HillClimbing hillClimbing = new HillClimbing(L, N, fitnessMap);
        HillClimbingBreadth hillClimbingBreadth = new HillClimbingBreadth(L, N, fitnessMap);

        double globalMaxFitness = Double.NEGATIVE_INFINITY;
        String globalBestSolution = "";

        Random random = new Random();

        for (int i = 0; i < RUNS; i++) {
            int methodChoice = random.nextInt(3); // Случайный выбор метода

            Result currentResult = null;

            System.out.println("========== Запуск " + (i + 1) + " ==========");

            switch (methodChoice) {
                case 0:
                    System.out.println("Запуск метода Монте-Карло");
                    currentResult = monteCarloMethod.runMonteCarlo();
                    break;
                case 1:
                    System.out.println("Запуск метода восхождения на холм (глубина)");
                    currentResult = hillClimbing.runHillClimbing();
                    break;
                case 2:
                    System.out.println("Запуск метода восхождения на холм (ширина)");
                    currentResult = hillClimbingBreadth.runHillClimbingBreadth();
                    break;
            }

            // Сравнение текущего результата с глобальным максимумом
            if (currentResult.maxFitness > globalMaxFitness) {
                globalMaxFitness = currentResult.maxFitness;
                globalBestSolution = currentResult.bestEncoding;
            }

            System.out.println("Текущий лучший результат: Кодировка = " + currentResult.bestEncoding +
                    ", Приспособленность = " + String.format("%.2f", currentResult.maxFitness));
            System.out.println("-------------------------------------------------------");
        }

        System.out.println("\nЛучший результат за все запуски: ");
        System.out.println("Кодировка = " + globalBestSolution + ", Приспособленность = " + String.format("%.2f", globalMaxFitness));
    }

    // Метод для генерации пространства поиска
    private static List<Integer> generateSearchSpace(int L) {
        int max = (int) Math.pow(2, L);
        List<Integer> space = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            space.add(i);
        }
        return space;
    }

    // Метод для генерации ландшафта приспособленности
    private static Map<Integer, Double> generateFitnessMap(List<Integer> space) {
        Map<Integer, Double> fitness = new LinkedHashMap<>(); // Используем LinkedHashMap для сохранения порядка
        for (int x : space) {
            fitness.put(x, calculateFitness(x));
        }
        return fitness;
    }

    // Функция приспособленности
    private static double calculateFitness(int x) {
        if (x == 0) return 0; // избегаем log(0)
        return 5 * Math.sin(Math.toRadians(x)) + Math.log(x);
    }

    // Метод для преобразования числа в двоичную строку заданной длины
    private static String toBinaryString(int num, int length) {
        String binary = Integer.toBinaryString(num);
        while (binary.length() < length) {
            binary = "0" + binary;
        }
        return binary;
    }

    // Метод для выбора случайных уникальных ключей из множества
    private static List<Integer> selectRandomKeys(Set<Integer> keys, int numberOfKeys) {
        List<Integer> keyList = new ArrayList<>(keys);
        //Collections.shuffle(keyList);
        return keyList.subList(0, Math.min(numberOfKeys, keyList.size()));
    }
}
