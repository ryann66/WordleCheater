package com.example.wordlecheater.wordleSolver;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Random;

/**
 * Implementation of WordleSolver that randomly picks a word from the list of possible answers each time
 */
public class RandomSolver extends AbstractWordleSolver{
    Random random = new Random();
    public RandomSolver(AssetManager am) throws IOException {
        super(am);
    }

    public String getBestWord() {
        return possibleAnswers.get(random.nextInt(possibleAnswers.size()));
    }
}
