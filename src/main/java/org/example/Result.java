package org.example;

public class Result {
    String bestEncoding;
    double maxFitness;

    public Result(String bestEncoding, double maxFitness) {
        this.bestEncoding = bestEncoding;
        this.maxFitness = maxFitness;
    }

    @Override
    public String toString() {
        return "Кодировка = " + bestEncoding + ", Приспособленность = " + maxFitness;
    }
}