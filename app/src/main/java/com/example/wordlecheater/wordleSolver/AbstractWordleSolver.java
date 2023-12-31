/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater.wordleSolver;

import java.util.*;

public abstract class AbstractWordleSolver implements WordleSolver{
    private static final int NUM_CHARACTERS = 26, WORD_LENGTH = 5;
    protected List<String> possibleGuesses, possibleAnswers, possibleAnswersSource;
    protected CharConstraint[] constraints;

    protected AbstractWordleSolver(List<String> possibleGuesses, List<String> possibleAnswers){
        this.possibleGuesses = possibleGuesses;
        this.possibleAnswers = possibleAnswers;
        this.possibleAnswersSource = List.copyOf(possibleAnswers);
        this.constraints = new CharConstraint[NUM_CHARACTERS];
    }

    @Override
    public boolean validGuess(String guess){
        return possibleGuesses.contains(guess);
    }

    @Override
    public void reset() {
        possibleAnswers = new ArrayList<>(possibleAnswersSource);
        for(int i = 0; i < NUM_CHARACTERS; i++){
            constraints[i] = new CharConstraint();
        }
    }

    @Override
    public boolean addConstraints(char[] cSet, TileStyle[] tsSet) {
        //copy constraints locally (to save state)
        CharConstraint[] constraints = new CharConstraint[this.constraints.length];
        for(int i = 0; i < constraints.length; i++)
            constraints[i] = new CharConstraint(this.constraints[i]);

        //update constraints locally with new information
        for(int i = 0; i < cSet.length; i++){
            if(tsSet[i] == TileStyle.YELLOW || tsSet[i] == TileStyle.GREEN){
                char c = cSet[i];
                constraints[c - 'a'].min += (WORD_LENGTH + 1);//shift index down
                if(tsSet[i] == TileStyle.GREEN) constraints[c - 'a'].presentAt.add(i);
                else constraints[c - 'a'].notPresentAt.add(i);
            }
        }
        for(int i = 0; i < cSet.length; i++){
            char c = cSet[i];
            //fix maxes
            if(constraints[c - 'a'].min > WORD_LENGTH){
                constraints[c - 'a'].min = Math.max(
                        constraints[c - 'a'].min / (WORD_LENGTH + 1),
                        constraints[c - 'a'].min % (WORD_LENGTH + 1));
            }
            //check mins
            if(tsSet[i] == TileStyle.GRAY){
                constraints[c - 'a'].max = constraints[c - 'a'].min;
                constraints[c - 'a'].notPresentAt.add(i);
            }
        }

        //check for impossibility in local constraints, only check characters that added new constraints
        //return false and discard constraints if impossible
        //check for impossibility in updated local constraints
        for(char c : cSet){
            int i = c - 'a';
            //cannot need more than allowed
            if(constraints[i].max < constraints[i].min) return false;
            //cannot have a character present at most spots than instances are allowed in word
            if(constraints[i].presentAt.size() > constraints[i].max) return false;
            //cannot need more instances of a character than spots that it is allowed to exist at
            if(WORD_LENGTH - constraints[i].notPresentAt.size() < constraints[i].min) return false;
            //not present list cannot contain any of the positions in the present list
            for(int p : constraints[i].presentAt)
                if(constraints[i].notPresentAt.contains(p)) return false;
        }

        //update global constraints field
        this.constraints = constraints;

        //remove all words that don't match the constraints
        Iterator<String> iter = possibleAnswers.iterator();
        iterLoop: while(iter.hasNext()){
            String str = iter.next();
            //check str matches location constraints
            for(int i = 0; i < constraints.length; i++){
                char c = (char) ('a' + i);
                for(Integer pos : constraints[i].presentAt)
                    if(str.charAt(pos) != c){
                        iter.remove();
                        continue iterLoop;
                    }
                for(Integer pos : constraints[i].notPresentAt)
                    if(str.charAt(pos) == c) {
                        iter.remove();
                        continue iterLoop;
                    }
            }

            //check character counts are valid
            int[] charCounts = new int[constraints.length];
            for(char c : str.toCharArray()){
                charCounts[c - 'a']++;
            }
            for(int i = 0; i < constraints.length; i++){
                if(charCounts[i] < constraints[i].min || charCounts[i] > constraints[i].max){
                    iter.remove();
                    continue iterLoop;
                }
            }
        }

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

    static protected class CharConstraint{
        public int min = 0;
        public int max = WORD_LENGTH;
        public Set<Integer> presentAt;
        public Set<Integer> notPresentAt;

        public CharConstraint(){
            this.presentAt = new HashSet<>();
            this.notPresentAt = new HashSet<>();
        }

        public CharConstraint(CharConstraint cc){
            this.min = cc.min;
            this.max = cc.max;
            this.presentAt = new HashSet<>(cc.presentAt);
            this.notPresentAt = new HashSet<>(cc.notPresentAt);
        }
    }
}
