/*
 * Author: Ryan Nelson
 */

import com.example.wordlecheater.MainActivity;
import com.example.wordlecheater.wordleSolver.Wordle;
import com.example.wordlecheater.wordleSolver.*;
import org.junit.Test;

import java.io.PrintStream;
import java.util.*;

import static java.lang.Double.NaN;

public class WordleSolverEvaluator {

    /**
     * Tests for evaluating wordleSolvers
     */
    @Test
    public void evaluateWordleSolvers() {
        PrintStream log = System.err;

        List<String> targetWords = new ArrayList<>();
        Scanner listReader = new Scanner(this.getClass().getResourceAsStream("res/raw/validanswers.txt"));
        while(listReader.hasNextLine()) targetWords.add(listReader.nextLine());
        listReader.close();

        targetWords = Collections.unmodifiableList(targetWords);
        List<String> possibleAnswers = List.copyOf(targetWords), possibleGuesses = new ArrayList<>(13000);

        listReader = new Scanner(this.getClass().getResourceAsStream("res/raw/validguesses.txt"));
        while(listReader.hasNextLine()) possibleGuesses.add(listReader.nextLine());
        listReader.close();

        evaluateWordleSolver(new InformationSolver(possibleGuesses, possibleAnswers), targetWords, log);
    }

    private boolean solved(TileStyle[] ts){
        for(TileStyle t : ts){
            if(t != TileStyle.GREEN) return false;
        }
        return true;
    }

    /**
     * Evaluates the given WordleSolver on all target words in the given list
     * @param wordleSolver the model to test
     * @param targetWords the list of words to test the mode on finding
     * @param log where to log output to
     * @return the average score the model received
     */
    public double evaluateWordleSolver(WordleSolver wordleSolver, List<String> targetWords, PrintStream log){
        int wordsSolved = 0;
        int guessesTaken = 0;
        try{
            for(String word : targetWords){
                Wordle wordle = new Wordle(word);
                Wordle.GuessResult ret = wordle.guess(wordleSolver.getBestWord());
                while (!wordle.isComplete()){
                    if(!wordleSolver.addConstraints(ret.cResult, ret.tsResult)){
                        log.println();
                    }
                    ret = wordle.guess(wordleSolver.getBestWord());
                }
                if(solved(ret.tsResult)){
                    wordsSolved++;
                    guessesTaken += wordle.getGuessesTaken();
                    log.println(word + " solved in " + wordle.getGuessesTaken());
                }
                else log.println(word + " not solved");
                wordleSolver.reset();
            }
            log.println("Unsolved words: " + (targetWords.size() - wordsSolved));
            log.println("Score: " + ((double) guessesTaken) / wordsSolved);
            return ((double) guessesTaken) / wordsSolved;
        }catch(Exception e){
            log.println("Error while running test");
            return NaN;
        }
    }
}
