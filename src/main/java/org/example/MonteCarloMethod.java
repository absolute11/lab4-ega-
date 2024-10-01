package org.example;

import java.util.*;

public class MonteCarloMethod {

    private int L;
    private int N;
    private int B;
    private Map<Integer, Double> landscape;
    private List<Integer> searchSpace;

    public MonteCarloMethod(int L, int N, Map<Integer, Double> landscape) {
        this.L = L;
        this.N = N;
        this.B = (int) Math.pow(2, L) - 1;
        this.landscape = landscape;
        this.searchSpace = new ArrayList<>(landscape.keySet());
    }

    public Result runMonteCarlo() {
        Random random = new Random();
        double maxFitness = Double.NEGATIVE_INFINITY;
        String bestEncoding = "";

        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "Шаг", "Кодировка", "Приспособленность", "Текущий максимум", "Лучшая кодировка");

        for (int i = 0; i < N; i++) {
            int encoding = searchSpace.get(random.nextInt(searchSpace.size()));
            String binaryString = String.format("%" + L + "s", Integer.toBinaryString(encoding)).replace(' ', '0');
            double fitness = landscape.get(encoding);

            if (fitness > maxFitness) {
                maxFitness = fitness;
                bestEncoding = binaryString;
            }

            System.out.printf("%-10d %-20s %-20.2f %-20.2f %-20s%n", i + 1, binaryString, fitness, maxFitness, bestEncoding);
        }

        return new Result(bestEncoding, maxFitness);
    }

    private Map<String, Double> generateLandscape() {
        Map<String, Double> landscape = new LinkedHashMap<>();

        for (int i = 0; i < N; i++) {
            int encoding = new Random().nextInt(B + 1);
            String binaryString = String.format("%" + L + "s", Integer.toBinaryString(encoding)).replace(' ', '0');
            double fitness = calculateFitness(encoding);
            landscape.put(binaryString, fitness);
        }

        return landscape;
    }


    private double calculateFitness(int x) {
        if (x == 0) return 0; // избегаем log(0)
        return 5 * Math.sin(x) + Math.log(x);
    }
}
