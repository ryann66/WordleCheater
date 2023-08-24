import com.example.wordlecheater.wordleSolver.TileStyle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Testing class for playing a game of Wordle
 * One instance of the class represents one singular game and cannot be reset
 */
public class Wordle {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_GUESSES = 6;
    //set of valid words and initializer
    private static final Set<String> allWords;
    static {
        allWords = new HashSet<>();
        try{
            Scanner listReader = new Scanner(Wordle.class.getClassLoader().getResourceAsStream("res/raw/allwords.txt"));
            while(listReader.hasNextLine()) allWords.add(listReader.nextLine());
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
        if(word.length() != WORD_LENGTH || !allWords.contains(word))
            throw new IllegalArgumentException("Invalid word");
        char[] cRet = word.toCharArray();
        // assert cRet.length == WORD_LENGTH;
        TileStyle[] tsRet = new TileStyle[WORD_LENGTH];
        String tmpTarget = target;

        for(int i = WORD_LENGTH - 1; i >= 0; i--){
            if(word.charAt(i) == tmpTarget.charAt(i)){
                tmpTarget = tmpTarget.substring(0, i) + tmpTarget.substring(i + 1);
                tsRet[i] = TileStyle.GREEN;
            }
            else tsRet[i] = TileStyle.EMPTY;
        }
        for(int i = 0; i < WORD_LENGTH; i++){
            if(tsRet[i] == TileStyle.GREEN) continue;
            if(tmpTarget.indexOf(word.charAt(i)) != -1){
                tsRet[i] = TileStyle.YELLOW;
                tmpTarget = tmpTarget.replaceFirst(((Character)word.charAt(i)).toString(), "");
            }
            else tsRet[i] = TileStyle.GRAY;
        }

        guesses++;
        complete = guesses >= MAX_GUESSES;
        for(TileStyle ts : tsRet){
            if(ts != TileStyle.GREEN) return new GuessResult(cRet, tsRet);
        }
        //all greens
        complete = true;
        return new GuessResult(cRet, tsRet);
    }

    /**
     *  immutable class for returning the result of a guess
     */
    public static class GuessResult {
        public final char[] cResult;
        public final TileStyle[] tsResult;
        public GuessResult(char[] cResult, TileStyle[] tsResult) {
            if (cResult.length != WORD_LENGTH || tsResult.length != WORD_LENGTH)
                throw new IllegalArgumentException("Incorrect length of arrays");
            this.cResult = cResult;
            this.tsResult = tsResult;
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
