/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater.wordleSolver;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Testing class for playing a game of com.example.wordlecheater.wordleSolver.Wordle
 * One instance of the class represents one singular game and cannot be reset
 */
public class Wordle {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_GUESSES = 6;
    //set of valid words and initializer
    private static final Set<String> allWords;
    static {
        allWords = new HashSet<>();
        try{
            Scanner listReader = new Scanner(Wordle.class.getClassLoader().getResourceAsStream("res/raw/validguesses.txt"));
            while(listReader.hasNextLine()) allWords.add(listReader.nextLine());
        }catch(Exception e){
            throw new RuntimeException("Resource loading failure", e.getCause());
        }
    }

    /**
     * Returns the clues based on the input guess
     * @param target the target word trying to be guessed
     * @param word the guess inputted
     * @return GuessResult with the clues given from the guess
     * @throws IllegalArgumentException if word is not a valid com.example.wordlecheater.wordleSolver.Wordle guess
     */
    public static GuessResult guess(String target, String word){
        if(word.length() != WORD_LENGTH || !allWords.contains(word))
            throw new IllegalArgumentException("Invalid word");
        char[] cRet = word.toCharArray();
        // assert cRet.length == WORD_LENGTH;
        TileStyle[] tsRet = new TileStyle[WORD_LENGTH];
        String tmpTarget = target;

        for(int i = WORD_LENGTH - 1; i >= 0; i--){
            if(word.charAt(i) == tmpTarget.charAt(i)){
                tmpTarget = tmpTarget.substring(0, i) + tmpTarget.substring(i + 1);
                tsRet[i] = TileStyle.GREEN;
            }
            else tsRet[i] = TileStyle.EMPTY;
        }
        for(int i = 0; i < WORD_LENGTH; i++){
            if(tsRet[i] == TileStyle.GREEN) continue;
            int index = tmpTarget.indexOf(word.charAt(i));
            if(index != -1){
                tsRet[i] = TileStyle.YELLOW;
                tmpTarget = (new StringBuilder(tmpTarget)).deleteCharAt(index).toString();
                //tmpTarget = tmpTarget.replaceFirst(((Character)word.charAt(i)).toString(), "");
            }
            else tsRet[i] = TileStyle.GRAY;
        }
        return new GuessResult(cRet, tsRet);
    }

    public Wordle(String targetWord){
        target = targetWord;
    }

    private final String target;
    private int guesses = 0;
    private boolean complete = false;

    public int getGuessesTaken(){
        return guesses;
    }

    public boolean isComplete(){
        return complete;
    }

    /**
     * Returns the clues given based on the input guess
     * @param word the guessed word
     * @return the results of the guess, per com.example.wordlecheater.wordleSolver.Wordle rules
     * @throws IllegalArgumentException if word is not a valid com.example.wordlecheater.wordleSolver.Wordle guess
     * @throws GameOverError if the game is over
     */
    public GuessResult guess(String word){
        if(complete) throw new GameOverError("Game is over");
        GuessResult grRet = guess(this.target, word);
        TileStyle[] tsRet = grRet.tsResult;
        guesses++;
        complete = guesses >= MAX_GUESSES;
        for(TileStyle ts : tsRet){
            if(ts != TileStyle.GREEN) return grRet;
        }
        //all greens
        complete = true;
        return grRet;
    }

    /**
     *  immutable class for returning the result of a guess
     */
    public static class GuessResult {
        public final char[] cResult;
        public final TileStyle[] tsResult;
        public GuessResult(char[] cResult, TileStyle[] tsResult) {
            if (cResult.length != WORD_LENGTH || tsResult.length != WORD_LENGTH)
                throw new IllegalArgumentException("Incorrect length of arrays");
            this.cResult = cResult;
            this.tsResult = tsResult;
        }
        //returns the hashcode for the guess result
        //hashcode is not evaluated with respect to cResult, only tsResult
        //returns an integer between 0 (inclusive) and 3^WORD_LENGTH (exclusive)
        public int hashCode(){
            int sum = 0;
            for(TileStyle ts : tsResult){
                sum *= 3;
                switch (ts) {
                    case GREEN -> sum += 2;
                    case YELLOW -> sum++;
                }
            }
            return sum;
        }
    }

    /**
     * Error for attempting to guess when game is over
     */
    public static class GameOverError extends RuntimeException{
        public GameOverError(){
            super();
        }

        public GameOverError(String message){
            super(message);
        }
    }
}
