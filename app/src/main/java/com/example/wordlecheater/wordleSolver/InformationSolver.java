/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater.wordleSolver;

/**
 * Implementation of WordleSolver that picks a word from the list of possible answers algorithmically
 */
public class InformationSolver extends AbstractWordleSolver{
    private boolean firstWord;
    private final String bestFirstWord;

    public InformationSolver() {
        super();
        firstWord = true;
        bestFirstWord = calculateBestWord();
    }

    @Override
    public String getBestWord() {
        //if first word, use precalculated answer
        if(firstWord) return bestFirstWord;
        return calculateBestWord();
    }

    @Override
    public void reset() {
        firstWord = true;
        super.reset();
    }

    @Override
    public boolean addConstraints(char[] cSet, TileStyle[] tsSet) {
        firstWord = false;
        return super.addConstraints(cSet, tsSet);
    }

    //evaluates all words in possible guesses to find the best guess
    private String calculateBestWord() {
        if(noWords()) throw new IllegalStateException("No words remaining");

        //take 50/50 chance if there are only two possibilities
        if(possibleAnswers.size() <= 2) return possibleAnswers.get(0);
        //todo: always guess from set of possible answers if only a few possibilities remain

        //get the best possible word from all possible guesses
        String bestWord = possibleGuesses.get(0);
        double bestInfo = evaluateWord(possibleGuesses.get(0));
        for(String possibleGuess : possibleGuesses){
            double newInfo = evaluateWord(possibleGuess);
            if(newInfo > bestInfo){
                bestWord = possibleGuess;
                bestInfo = newInfo;
            }
        }

        return bestWord;
    }

    // evaluates the given word and returns the expected information from it
    // Algorithm loosely based on 3Blue1Brown's solution in https://www.youtube.com/watch?v=v68zYyaEmEA
    private double evaluateWord(String word){
        //get the frequencies of each possible guess pattern that wordle can return
        int[] possiblePatterns = new int[(int)Math.pow(3, Wordle.WORD_LENGTH)];
        for(String possibleAnswer : possibleAnswers){
            possiblePatterns[Wordle.guess(possibleAnswer, word).hashCode()]++;
        }
        //calculate the information gained from each pattern and the probability of the guess being selected
        //sum the information for each guess
        double informationSum = 0;
        for(int i = 0; i < possiblePatterns.length; i++){
            double probability = ((double)possiblePatterns[i]) / possibleAnswers.size();
            if(probability == 0) continue;//no information to be gained from impossible outcome
            double information = probability * -(Math.log(probability) / Math.log(2));
            informationSum += information;
        }
        return informationSum;
    }
}
