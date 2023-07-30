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
    public boolean addConstraints(char c1, MainActivity.TileStyle ts1,
                                  char c2, MainActivity.TileStyle ts2,
                                  char c3, MainActivity.TileStyle ts3,
                                  char c4, MainActivity.TileStyle ts4,
                                  char c5, MainActivity.TileStyle ts5);

    /**
     * Returns true if there is only one word left that it can be
     */
    public boolean lastWord();

    /**
     * Returns true if there are no words left that it can be
     */
    public boolean noWords();
}
