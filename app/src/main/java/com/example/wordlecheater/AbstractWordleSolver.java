package com.example.wordlecheater;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.*;

public abstract class AbstractWordleSolver implements  WordleSolver{
    private static int NUM_CHARACTERS = 26;
    protected List<String> possibleGuesses, possibleAnswers;
    protected AssetManager assetManager;
    protected CharConstraint[] constraints;

    protected AbstractWordleSolver(AssetManager am) throws IOException {
        assetManager = am;
        Scanner input = new Scanner(am.open("validwords.txt"));
        possibleAnswers = new ArrayList<>(2400);
        while(input.hasNextLine()) possibleAnswers.add(input.nextLine());
        input = new Scanner(am.open("allwords.txt"));
        possibleGuesses = new ArrayList<>(13000);
        while(input.hasNextLine()) possibleGuesses.add(input.nextLine());
        possibleGuesses = Collections.unmodifiableList(possibleGuesses);
        constraints = new CharConstraint[NUM_CHARACTERS];
        for(int i = 0; i < NUM_CHARACTERS; i++){
            constraints[i] = new CharConstraint();
        }
    }

    @Override
    public void reset() {
        try{
            Scanner input = new Scanner(assetManager.open("validwords.txt"));
            possibleAnswers.clear();
            while(input.hasNextLine()) possibleAnswers.add(input.nextLine());
            input = new Scanner(assetManager.open("allwords.txt"));
            for(int i = 0; i < NUM_CHARACTERS; i++){
                constraints[i] = new CharConstraint();
            }
        }catch(IOException ioe) {
            System.exit(1);
        }
    }

    @Override
    public boolean addConstraints(char[] cSet, MainActivity.TileStyle[] tsSet) {
        //copy constraints (to save state)
        CharConstraint[] constraints = new CharConstraint[this.constraints.length];
        for(int i = 0; i < constraints.length; i++)
            constraints[i] = new CharConstraint(this.constraints[i]);

        //update constraints locally
        for(int i = 0; i < cSet.length; i++){
            if(tsSet[i] == MainActivity.TileStyle.YELLOW || tsSet[i] == MainActivity.TileStyle.GREEN){
                char c = cSet[i];
                constraints[c - 'a'].max++;//shift index down
                if(tsSet[i] == MainActivity.TileStyle.GREEN) constraints[c - 'a'].presentAt.add(i);
                else constraints[c - 'a'].notPresentAt.add(i);
            }
        }
        for(int i = 0; i < cSet.length; i++){
            if(tsSet[i] == MainActivity.TileStyle.GRAY){
                char c = cSet[i];
                constraints[c - 'a'].max = constraints[c - 'a'].min;
                constraints[c - 'a'].notPresentAt.add(i);
            }
        }

        //check for impossibility in local constraints
        for(int i = 0; i < constraints.length; i++){//todo restrict to only updated characters
            //cannot need more than allowed
            if(constraints[i].max < constraints[i].min) return false;
            //cannot have a character present at most spots than instances are allowed in word
            if(constraints[i].presentAt.size() > constraints[i].max) return false;
            //cannot need more instances of a character than spots that it is allowed to exist at
            if(MainActivity.WORD_LENGTH - constraints[i].notPresentAt.size() < constraints[i].min) return false;
            //not present list cannot contain any of the positions in the present list
            for(int p : constraints[i].presentAt)
                if(constraints[i].notPresentAt.contains(p)) return false;
        }

        //update global constraints
        this.constraints = constraints;

        //remove all words that don't match the constraints
        //todo remove all words that are impossible to be correct


        return true;
    }

    @Override
    public boolean lastWord() {
        return possibleAnswers.size() == 1;
    }

    @Override
    public boolean noWords(){
        return possibleAnswers.isEmpty();
    }

    protected class CharConstraint{
        public int min = 0;
        public int max = MainActivity.WORD_LENGTH;
        public Set<Integer> presentAt = new HashSet<>();
        public Set<Integer> notPresentAt = new HashSet<>();

        public CharConstraint(){}

        public CharConstraint(CharConstraint cc){
            this.min = cc.min;
            this.max = cc.max;
            this.presentAt = new HashSet<>(cc.presentAt);
            this.notPresentAt = new HashSet<>(cc.notPresentAt);
        }
    }
}
