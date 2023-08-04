package com.example.wordlecheater.wordleSolver;

import java.util.Random;

/**
 * Implementation of WordleSolver that randomly picks a word from the list of possible answers each time
 */
public class RandomSolver extends AbstractWordleSolver{
    Random random = new Random();
    public RandomSolver() {
        super();
    }

    public String getBestWord() {
        return possibleAnswers.get(random.nextInt(possibleAnswers.size()));
    }
}