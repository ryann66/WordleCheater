/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater.wordleSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Implementation of WordleSolver that picks a word from the list of possible answers algorithmically
 */
public class InformationSolver extends AbstractWordleSolver{
    private static final int MAX_THREADS = 6, MIN_WORDS_PER_THREAD = 500;
    private boolean firstWord;
    private final String bestFirstWord;

    /**
     * Slow constructor that calculates the best first word to play in Wordle
     */
    InformationSolver(List<String> possibleGuesses, List<String> possibleAnswers) {
        super(possibleGuesses, possibleAnswers);
        firstWord = true;
        bestFirstWord = calculateBestWord();
    }

    /**
     * Fast constructor that sets the first word based off client arguments
     * @param firstWord the best first word to play in Wordle
     */
    InformationSolver(List<String> possibleGuesses, List<String> possibleAnswers, String firstWord) {
        super(possibleGuesses, possibleAnswers);
        if(!validGuess(firstWord)) throw new IllegalArgumentException("Invalid starting guess");
        this.firstWord = true;
        this.bestFirstWord = firstWord;
    }

    @Override
    public String getBestWord() {
        //if first word, use precalculated answer
        if(firstWord) return bestFirstWord;
        return calculateBestWord();
    }

    @Override
    public void reset() {
        firstWord = true;
        super.reset();
    }

    @Override
    public boolean addConstraints(char[] cSet, TileStyle[] tsSet) {
        firstWord = false;
        return super.addConstraints(cSet, tsSet);
    }

    //evaluates all words in possible guesses to find the best guess
    private String calculateBestWord() {
        if(noWords()) throw new IllegalStateException("No words remaining");
        //todo: improve algorithm(?)

        //only guess possible answers
        if(possibleAnswers.size() <= 6){
            CalculateBestWordThread bestWordFinderThread = new CalculateBestWordThread(possibleAnswers, possibleAnswers);
            bestWordFinderThread.run();
            if(possibleAnswers.size() <= 3 || bestWordFinderThread.getBestInfo() >= 1) return bestWordFinderThread.getBestWord();
        }

        //use multithreading
        //calculate number of threads
        int numThreads = MAX_THREADS;
        while(possibleGuesses.size() / numThreads < MIN_WORDS_PER_THREAD) numThreads--;
        //set up threads
        List<CalculateBestWordThread> threadList = new ArrayList<>(numThreads);
        int length = possibleGuesses.size() / numThreads;//number of elements in most threads
        int numLengthed = possibleGuesses.size() % numThreads;//number of threads that need another element added
        int curIndex = 0;
        for(int i = 0; i < numThreads; i++){
            int tmpLength = length;
            if(numLengthed > 0) {
                tmpLength++;
                numLengthed--;
            }
            CalculateBestWordThread cbwt = new CalculateBestWordThread(possibleGuesses, possibleAnswers, curIndex,
                    curIndex + tmpLength);
            cbwt.start();
            threadList.add(cbwt);
            curIndex += tmpLength;
        }

        //wait for threads
        String bestWord = possibleGuesses.get(0);
        double bestInfo = evaluateWord(possibleAnswers, possibleGuesses.get(0));
        ListIterator<CalculateBestWordThread> threads = threadList.listIterator();
        while(threads.hasNext()){
            CalculateBestWordThread thread = threads.next();
            try{
                thread.join();
                if(thread.getBestInfo() > bestInfo){
                    bestWord = thread.getBestWord();
                    bestInfo = thread.getBestInfo();
                }
            }catch(InterruptedException ie){
                CalculateBestWordThread cbwt = new CalculateBestWordThread(thread.possibleGuesses,
                        thread.possibleAnswers, thread.startIndex, thread.endIndex);
                cbwt.start();
                threads.add(cbwt);
            }
        }
        return bestWord;
    }

    private static class CalculateBestWordThread extends Thread {
        private final List<String> possibleGuesses, possibleAnswers;
        private final int startIndex, endIndex;
        private double bestInfo = 0;
        private String bestWord = null;

        /**
         * Creates a new instance that evaluates all the possibleGuesses in possibleGuesses
         * Does not modify lists
         * @param possibleGuesses list of words that can be guessed in wordle
         * @param possibleAnswers list of words that can be the wordle solution
         * @throws IllegalArgumentException unless 0 <= startIndex <= endIndex <= possibleGuesses.size()
         */
        public CalculateBestWordThread(List<String> possibleGuesses, List<String> possibleAnswers){
            this(possibleGuesses, possibleAnswers, 0, possibleGuesses.size());
        }

        /**
         * Creates a new instance with constrained boundaries
         * Does not modify lists
         * @param possibleGuesses list of words that can be guessed in wordle
         * @param possibleAnswers list of words that can be the wordle solution
         * @param startIndex inclusive start index of possibleGuesses to evaluate
         * @param endIndex exclusive end index of possibleGuesses to evaluate
         * @throws IllegalArgumentException unless 0 <= startIndex <= endIndex <= possibleGuesses.size()
         */
        public CalculateBestWordThread(List<String> possibleGuesses, List<String> possibleAnswers, int startIndex, int endIndex){
            if(startIndex < 0 || startIndex > endIndex || endIndex > possibleGuesses.size()) throw new IllegalArgumentException();
            this.possibleAnswers = possibleAnswers;
            this.possibleGuesses = possibleGuesses;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            bestWord = possibleGuesses.get(startIndex);
            bestInfo = evaluateWord(possibleAnswers, possibleGuesses.get(startIndex));
            for(int i = startIndex; i < endIndex; i++){
                String guess = possibleGuesses.get(i);
                double newInfo = evaluateWord(possibleAnswers, guess);
                if(newInfo > bestInfo){
                    bestWord = guess;
                    bestInfo = newInfo;
                }
            }
        }

        /**
         * Returns the most amount of information that any of the guesses evaluated to
         * @return the most amount of information that any of the guesses evaluated to
         */
        public double getBestInfo(){
            return bestInfo;
        }

        /**
         * Returns the guess that evaluated to have the most information
         * @return the guess that evaluated to have the most information
         * @throws IllegalStateException if run() has not been called or if no best word was found
         */
        public String getBestWord(){
            if(bestWord == null) throw new IllegalStateException();
            return bestWord;
        }
    }

    // evaluates the given word and returns the expected information from it
    // Algorithm loosely based on 3Blue1Brown's solution in https://www.youtube.com/watch?v=v68zYyaEmEA
    private static double evaluateWord(List<String> possibleAnswers, String word){
        //get the frequencies of each possible guess pattern that wordle can return
        int[] possiblePatterns = new int[(int)Math.pow(3, Wordle.WORD_LENGTH)];
        for(String possibleAnswer : possibleAnswers){
            possiblePatterns[Wordle.guess(possibleAnswer, word).hashCode()]++;
        }
        //calculate the information gained from each pattern and the probability of the guess being selected
        //sum the information for each guess
        double informationSum = 0;
        for (int possiblePattern : possiblePatterns) {
            double probability = ((double) possiblePattern) / possibleAnswers.size();
            if (probability == 0) continue;//no information to be gained from impossible outcome
            double information = probability * -(Math.log(probability) / Math.log(2));
            informationSum += information;
        }
        return informationSum;
    }
}
