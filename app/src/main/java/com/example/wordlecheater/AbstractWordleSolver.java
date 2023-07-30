package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractWordleSolver implements  WordleSolver{
    protected List<String> possibleGuesses, possibleAnswers;

    protected AbstractWordleSolver(AssetManager am) throws IOException {
        Scanner input = new Scanner(am.open("validwords.txt"));
        possibleAnswers = new ArrayList<>(2400);
        while(input.hasNextLine()) possibleAnswers.add(input.nextLine());
        input = new Scanner(am.open("allwords.txt"));
        possibleGuesses = new ArrayList<>(13000);
        while(input.hasNextLine()) possibleGuesses.add(input.nextLine());
    }
}
