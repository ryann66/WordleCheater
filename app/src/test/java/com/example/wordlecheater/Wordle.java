package com.example.wordlecheater;

import com.example.wordlecheater.wordleSolver.TileStyle;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Class for playing a game of Wordle
 * One instance of the class represents one singular game and cannot be reset
 */
public class Wordle {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_GUESSES = 6;
    //set of valid words and initializer
    private static final Set<String> validWords;
    static {
        validWords = new HashSet<>();
        try{
            URL url = Wordle.class.getResource("validwords.txt");
            Scanner listReader = new Scanner(new File(url.toURI()));
            while(listReader.hasNextLine()) validWords.add(listReader.nextLine());
        }catch(Exception e){
            throw new RuntimeException("Resource loading failure", e.getCause());
        }

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
     * @return the results of the guess, per Wordle rules
     * @throws IllegalArgumentException if word is not a valid Wordle guess
     * @throws GameOverError if the game is over
     */
    public GuessResult guess(String word){
        if(complete) throw new GameOverError("Game is over");
        if(word.length() != WORD_LENGTH || !validWords.contains(word))
            throw new IllegalArgumentException("Invalid word");
        char[] cRet = new char[WORD_LENGTH];
        TileStyle[] tsRet = new TileStyle[WORD_LENGTH];
        Set<Integer> indices = new HashSet<>();
        for(int i = 0; i < 5; i++) indices.add(i);

        //handle greens
        Iterator<Integer> iter = indices.iterator();
        while(iter.hasNext()){
            int i = iter.next();
            if(word.charAt(i) == target.charAt(i)){
                iter.remove();
                cRet[i] = target.charAt(i);
                tsRet[i] = TileStyle.GREEN;
            }
        }

        //handle gray/yellows
        String tmpTarget = target;
        iter = indices.iterator();
        while(iter.hasNext()){
            int i = iter.next();
            iter.remove();

            char c = word.charAt(i);
            cRet[i] = c;

            int targetIndex = tmpTarget.indexOf(c);
            if(targetIndex == -1) {//gray
                tsRet[i] = TileStyle.GRAY;
            }else{//yellow
                tmpTarget = tmpTarget.replaceFirst(((Character)c).toString(), "");
                tsRet[i] = TileStyle.YELLOW;
            }
        }

        guesses++;
        complete = guesses >= MAX_GUESSES;
        if(!indices.isEmpty()) throw new AssertionError("Not all array indices handled");
        for(TileStyle ts : tsRet){
            if(ts != TileStyle.GREEN) return new GuessResult(cRet, tsRet);
        }
        //all greens
        complete = true;
        return new GuessResult(cRet, tsRet);
    }

    /**
     * Record for returning the result of a guess
     */
    public record GuessResult(char[] cResult, TileStyle[] tsResult) {
        public GuessResult {
            if (cResult.length != WORD_LENGTH || tsResult.length != WORD_LENGTH)
                throw new IllegalArgumentException("Incorrect length of arrays");
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