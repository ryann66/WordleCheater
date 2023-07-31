package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Random;

public class RandomWordleSolver extends AbstractWordleSolver{
    Random random = new Random();
    public RandomWordleSolver(AssetManager am) throws IOException {
        super(am);
    }

    public String getBestWord() {
        return possibleAnswers.get(random.nextInt(possibleAnswers.size()));
    }
}
