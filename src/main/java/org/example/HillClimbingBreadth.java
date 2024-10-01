package org.example;
import java.util.*;

public class HillClimbingBreadth {

    private int L;
    private int N;
    private Map<Integer, Double> fitnessMap;
    private List<Integer> searchSpace;
    private Random rand = new Random();

    public HillClimbingBreadth(int L, int N, Map<Integer, Double> fitnessMap) {
        this.L = L;
        this.N = N;
        this.fitnessMap = fitnessMap;
        this.searchSpace = new ArrayList<>(fitnessMap.keySet());
    }

    public Result runHillClimbingBreadth() {
        int step = 0;
        int currentSolution = searchSpace.get(rand.nextInt(searchSpace.size()));
        double currentFitness = fitnessMap.get(currentSolution);
        int maxSolution = currentSolution;
        double maxFitness = currentFitness;

        System.out.println("Начальная кодировка: " + toBinaryString(currentSolution, L) + " (μ = " + String.format("%.2f", currentFitness) + ")");

        while (step < N) {
            step++;

            List<Integer> neighborhood = getNeighborhood(maxSolution);
            System.out.println("Шаг " + step + ":");
            System.out.println("Текущий максимум maxS: " + toBinaryString(maxSolution, L) + " (μ = " + String.format("%.2f", maxFitness) + ")");
            System.out.println("Окрестность maxS:");
            for (int neighbor : neighborhood) {
                System.out.println("  " + toBinaryString(neighbor, L) + " (μ = " + String.format("%.2f", fitnessMap.get(neighbor)) + ")");
            }

            int bestNeighbor = maxSolution;
            double bestFitness = maxFitness;
            for (int neighbor : neighborhood) {
                double neighborFitness = fitnessMap.get(neighbor);
                if (neighborFitness > bestFitness) {
                    bestNeighbor = neighbor;
                    bestFitness = neighborFitness;
                }
            }

            if (bestFitness > maxFitness) {
                maxSolution = bestNeighbor;
                maxFitness = bestFitness;
                System.out.println("Смена maxS на: " + toBinaryString(maxSolution, L) + " (μ = " + String.format("%.2f", maxFitness) + ")");
                System.out.println("-------------------------------------------------------");
            } else {
                System.out.println("Локальный максимум достигнут.");
                break;
            }
        }

        System.out.println("Результат: maxS = " + toBinaryString(maxSolution, L) + " (μ = " + String.format("%.2f", maxFitness) + ")");

        return new Result(toBinaryString(maxSolution, L), maxFitness);
    }

    private List<Integer> getNeighborhood(int solution) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < L; i++) {
            int neighbor = solution ^ (1 << i);
            if (searchSpace.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private String toBinaryString(int num, int length) {
        String binary = Integer.toBinaryString(num);
        while (binary.length() < length) {
            binary = "0" + binary;
        }
        return binary;
    }
}