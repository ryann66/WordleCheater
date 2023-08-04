package com.example.wordlecheater.wordleSolver;

import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Implementation of WordleSolver that picks a word from the list of possible answers algorithmically
 */
public class InformationSolver extends AbstractWordleSolver{


    public InformationSolver(AssetManager am) throws IOException {
        super(am);
    }

    @Override
    public String getBestWord() {
        return null;
        //todo find the word in possible guesses that constrains possibleAnswers the most
    }
}
