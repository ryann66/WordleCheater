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
        if(noWords()) throw new IllegalStateException("No words remaining");
        //todo: ensure that picked word is a possible answer if possibleAnswers.size() is too small

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
    private double evaluateWord(String word){
        int[] possiblePatterns = new int[(int)Math.pow(3, Wordle.WORD_LENGTH)];
        for(String possibleAnswer : possibleAnswers){
            possiblePatterns[Wordle.guess(possibleAnswer, word).hashCode()]++;
        }
        //todo get average value of possiblePatterns
        //todo return value
    }
}
