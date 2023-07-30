package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Random;

public class RandomWordleSolver extends AbstractWordleSolver{
    Random random = new Random();
    public RandomWordleSolver(AssetManager am) throws IOException {
        super(am);
    }

    public void reset() {

    }

    public String getBestWord() {
        return possibleAnswers.get(random.nextInt(possibleAnswers.size()));
    }

    public boolean addConstraints(char c1, MainActivity.TileStyle ts1,
                                  char c2, MainActivity.TileStyle ts2,
                                  char c3, MainActivity.TileStyle ts3,
                                  char c4, MainActivity.TileStyle ts4,
                                  char c5, MainActivity.TileStyle ts5) {
        return true;
    }

    public void addConstraint() {

    }

    public boolean lastWord(){
        return false;
    }

    public boolean noWords(){
        return false;
    }
}
