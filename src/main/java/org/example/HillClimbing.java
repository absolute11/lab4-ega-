package org.example;
import java.util.*;

public class HillClimbing {

    private int L;
    private int N;
    private Map<Integer, Double> fitnessMap;
    private List<Integer> searchSpace;
    private Random rand = new Random();

    public HillClimbing(int L, int N, Map<Integer, Double> fitnessMap) {
        this.L = L;
        this.N = N;
        this.fitnessMap = fitnessMap;
        this.searchSpace = new ArrayList<>(fitnessMap.keySet());
    }

    public Result runHillClimbing() {
        int step = 0;
        int currentSolution = searchSpace.get(rand.nextInt(searchSpace.size()));
        double currentFitness = fitnessMap.get(currentSolution);
        int maxSolution = currentSolution;
        double maxFitness = currentFitness;

        System.out.println("Начальная кодировка: " + toBinaryString(currentSolution, L) + " (μ = " + String.format("%.2f", currentFitness) + ")");

        List<Integer> neighborhood = getNeighborhood(currentSolution);
        System.out.println("Окрестность начальной кодировки:");
        for (int neighbor : neighborhood) {
            System.out.println("  " + toBinaryString(neighbor, L) + " (μ = " + String.format("%.2f", fitnessMap.get(neighbor)) + ")");
        }
        System.out.println("-------------------------------------------------------");

        List<Integer> currentNeighborhood = new ArrayList<>(neighborhood);
        Set<Integer> checkedNeighbors = new HashSet<>();

        while (step < N) {
            if (currentNeighborhood.isEmpty()) {
                System.out.println("Локальный максимум достигнут. Окрестность пуста.");
                break;
            }

            Collections.shuffle(currentNeighborhood);
            boolean moved = false;

            for (Iterator<Integer> iterator = currentNeighborhood.iterator(); iterator.hasNext(); ) {
                int neighbor = iterator.next();

                if (checkedNeighbors.contains(neighbor)) {
                    continue;
                }

                step++;

                System.out.println("Текущий maxS: " + toBinaryString(maxSolution, L) + " (μ = " + String.format("%.2f", maxFitness) + ")");

                double neighborFitness = fitnessMap.get(neighbor);
                System.out.println("Шаг " + step + ": Выбираем s_i: " + toBinaryString(neighbor, L) + " (μ = " + String.format("%.2f", neighborFitness) + ")");

                if (neighborFitness > maxFitness) {
                    maxSolution = neighbor;
                    maxFitness = neighborFitness;
                    moved = true;
                    System.out.println("Смена maxS на: " + toBinaryString(maxSolution, L) + " (μ = " + String.format("%.2f", maxFitness) + ")");

                    // Обновляем окрестность для нового максимума
                    currentNeighborhood = getNeighborhood(maxSolution);
                    currentNeighborhood.removeAll(checkedNeighbors);
                    checkedNeighbors.clear();
                    currentNeighborhood.remove(Integer.valueOf(maxSolution));
                    System.out.println("Новая окрестность maxS:");
                    for (int newNeighbor : currentNeighborhood) {
                        System.out.println("  " + toBinaryString(newNeighbor, L) + " (μ = " + String.format("%.2f", fitnessMap.get(newNeighbor)) + ")");
                    }
                    System.out.println("-------------------------------------------------------");
                    break;
                } else {
                    checkedNeighbors.add(neighbor);
                }

                iterator.remove();
                System.out.println("Оставшиеся в окрестности (без текущего):");
                for (int remainingNeighbor : currentNeighborhood) {
                    System.out.println("  " + toBinaryString(remainingNeighbor, L) + " (μ = " + String.format("%.2f", fitnessMap.get(remainingNeighbor)) + ")");
                }
                System.out.println("-------------------------------------------------------");
            }

            if (currentNeighborhood.isEmpty() || checkedNeighbors.size() == neighborhood.size()) {
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