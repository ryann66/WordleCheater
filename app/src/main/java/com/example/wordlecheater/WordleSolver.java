package com.example.wordlecheater;

public interface WordleSolver {
    /**
     * Removes all constraints, restarting the class
     */
    public void reset();

    /**
     * Gets the current best word to play
     */
    public String getBestWord();

    /**
     * returns true if the constraints were added, false if they are inconsistent with prior constraints
     */
    public boolean addConstraints(char[] c, MainActivity.TileStyle[] ts);

    /**
     * Returns true if there is only one word left that it can be
     */
    public boolean lastWord();

    /**
     * Returns true if there are no words left that it can be
     */
    public boolean noWords();
}
