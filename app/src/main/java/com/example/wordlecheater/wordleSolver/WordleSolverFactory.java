package com.example.wordlecheater.wordleSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * WARNING: this class is platform dependent
 * Factory class to create WordleSolvers and remove platform dependent aspects
 */
public class WordleSolverFactory {
    //paths to access the resources
    private static final String VALID_GUESSES_PATH = "res/raw/allwords.txt",
            VALID_ANSWERS_PATH = "res/raw/validwords.txt";
    //approximations of word list lengths for faster initialization
    private static final int VALID_GUESSES_LENGTH = 13000, VALID_ANSWERS_LENGTH = 2315;

    /**
     * Returns the list of valid wordle guesses
     * @return the list of valid wordle guesses
     */
    private static List<String> getValidGuessesList(){
        Scanner listReader = new Scanner(WordleSolverFactory.class.getClassLoader().getResourceAsStream(VALID_GUESSES_PATH));
        List<String> validGuesses = new ArrayList<>(VALID_GUESSES_LENGTH);
        while(listReader.hasNextLine()) validGuesses.add(listReader.nextLine());
        listReader.close();
        return validGuesses;
    }

    /**
     * Returns the list of valid wordle answers
     * @return the list of valid wordle answers
     */
    private static List<String> getValidAnswersList(){
        Scanner listReader = new Scanner(WordleSolverFactory.class.getClassLoader().getResourceAsStream(VALID_ANSWERS_PATH));
        List<String> validAnswers = new ArrayList<>(VALID_ANSWERS_LENGTH);
        while(listReader.hasNextLine()) validAnswers.add(listReader.nextLine());
        listReader.close();
        return validAnswers;
    }

    /**
     * Creates a new InformationSolver
     * @return an InformationSolver
     */
    public static InformationSolver informationSolver(){
        try{
            return new InformationSolver(getValidGuessesList(), getValidAnswersList());
        } catch(NullPointerException npe) {
            throw new Error("Resource loading failure");
        }
    }
}
