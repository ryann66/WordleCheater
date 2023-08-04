package com.example.wordlecheater.wordleSolver;

/**
 * Implementation of WordleSolver that picks a word from the list of possible answers algorithmically
 */
public class InformationSolver extends AbstractWordleSolver{


    public InformationSolver() {
        super();
    }

    @Override
    public String getBestWord() {
        return null;
        //todo find the word in possible guesses that constrains possibleAnswers the most
    }
}
