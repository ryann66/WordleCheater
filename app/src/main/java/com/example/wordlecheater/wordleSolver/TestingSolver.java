package com.example.wordlecheater.wordleSolver;

/**
 * Implementation of WordleSolver that always picks the first word alphabetically from the list of
 * possible answers each time; intended for testing consistency.
 */
public class TestingSolver extends AbstractWordleSolver{
    public TestingSolver() {
        super();
    }

    public String getBestWord() {
        return possibleAnswers.get(0);
    }
}