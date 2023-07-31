package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractWordleSolver implements  WordleSolver{
    protected List<String> possibleGuesses, possibleAnswers;
    protected AssetManager assetManager;
    List<Character>[] greens = new List<>[MainActivity.WORD_LENGTH];
    List<Character>[] yellows = new List<>[MainActivity.WORD_LENGTH];
    List<Character> grays;

    protected AbstractWordleSolver(AssetManager am) throws IOException {
        assetManager = am;
        Scanner input = new Scanner(am.open("validwords.txt"));
        possibleAnswers = new ArrayList<>(2400);
        while(input.hasNextLine()) possibleAnswers.add(input.nextLine());
        input = new Scanner(am.open("allwords.txt"));
        possibleGuesses = new ArrayList<>(13000);
        while(input.hasNextLine()) possibleGuesses.add(input.nextLine());
        possibleGuesses = Collections.unmodifiableList(possibleGuesses);
        for(int i = 0; i < MainActivity.WORD_LENGTH; i++){
            greens[i] = new ArrayList<>();
            yellows[i] = new ArrayList<>();
        }
        grays = new ArrayList<>();
    }

    @Override
    public void reset() {
        try{
            Scanner input = new Scanner(assetManager.open("validwords.txt"));
            possibleAnswers.clear();
            while(input.hasNextLine()) possibleAnswers.add(input.nextLine());
            input = new Scanner(assetManager.open("allwords.txt"));
            for(int i = 0; i < MainActivity.WORD_LENGTH; i++){
                greens[i].clear();
                yellows[i].clear();
            }
            grays.clear();
        }catch(IOException ioe) {
            System.exit(1);
        }
    }

    @Override
    public boolean addConstraints(char[] c, MainActivity.TileStyle[] ts) {
        return false;
        //todo check constraint consistency
        //todo return false if inconsistent
        //todo remove all words that are impossible to be correct
    }

    @Override
    public boolean lastWord() {
        return possibleAnswers.size() == 1;
    }

    @Override
    public boolean noWords(){
        return possibleAnswers.isEmpty();
    }
}
