package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Implementation of WordleSolver that always picks the first word alphabetically from the list of
 * possible answers each time; intended for testing consistency.
 */
public class TestingSolver extends AbstractWordleSolver{
    public TestingSolver(AssetManager am) throws IOException {
        super(am);
    }

    public String getBestWord() {
        return possibleAnswers.get(0);
    }
}