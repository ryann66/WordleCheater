/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater.wordleSolver;

public interface WordleSolver {
    /**
     * Returns true if the given 5 letter string is a valid wordle guess
     * @param guess the string to check validity as a wordle guess for
     * @return true if the given 5 letter string is a valid wordle guess
     */
    public boolean validGuess(String guess);

    /**
     * Removes all constraints, restarting the class
     */
    public void reset();

    /**
     * Gets the current best word to play
     * @throws IllegalStateException if there are no valid words remaining
     */
    public String getBestWord();

    /**
     * returns true if the constraints were added, false if they are inconsistent with prior constraints
     */
    public boolean addConstraints(char[] c, TileStyle[] ts);

    /**
     * Returns true if there is only one word left that it can be
     */
    public boolean lastWord();

    /**
     * Returns true if there are no words left that it can be
     */
    public boolean noWords();
}
