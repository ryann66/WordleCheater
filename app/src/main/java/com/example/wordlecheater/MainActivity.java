/*
 * Author: Ryan Nelson
 */

package com.example.wordlecheater;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.ComponentActivity;
import com.example.wordlecheater.wordleSolver.*;

public class MainActivity extends ComponentActivity {

    //changing these will cause the program to not function, particularly UI elements
    public static final int NUM_GUESSES = 6, WORD_LENGTH = 5;

    //array of all the tileIds of the main tiles
    int[][] tileIds;
    int curRow = 0, curCol = 0;
    private final WordleSolver wordleSolver = new InformationSolver();

    /**
     * Initializes the tile arrays, sets event listeners for all the buttons/tiles
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init tiles arrays
        tileIds = new int[][]{
            {(R.id.tile00), (R.id.tile01), (R.id.tile02),
                (R.id.tile03), (R.id.tile04)},
            {(R.id.tile10), (R.id.tile11), (R.id.tile12),
                (R.id.tile13), (R.id.tile14)},
            {(R.id.tile20), (R.id.tile21), (R.id.tile22),
                (R.id.tile23), (R.id.tile24)},
            {(R.id.tile30), (R.id.tile31), (R.id.tile32),
                (R.id.tile33), (R.id.tile34)},
            {(R.id.tile40), (R.id.tile41), (R.id.tile42),
                (R.id.tile43), (R.id.tile44)},
            {(R.id.tile50), (R.id.tile51), (R.id.tile52),
                (R.id.tile53), (R.id.tile54)},
        };
        //set onclicklisteners
        for(int i = 0; i < NUM_GUESSES; i++){
            for(int j = 0; j < WORD_LENGTH; j++){
                findViewById(tileIds[i][j]).setOnClickListener(new TileButton(i, j));
            }
        }

        //add other buttons to onclicklisteners
        findViewById(R.id.advance).setOnClickListener(new AdvanceButton());
        findViewById(R.id.reset).setOnClickListener(new ResetButton());

        new ResetButton().onClick(null);
    }

    /**
     * Displays an alert dialog to the user with the given message
     * @param message the message to give the reader
     */
    public void alert(String message){
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle(R.string.alert_title);
        adb.setMessage(message);
        adb.setPositiveButton(R.string.alert_accept, (dialogInterface, i) -> { });
        adb.show();
    }

    /**
     * Types the character into the tile grid
     * @param c the character to add into the tile grid
     */
    public void addCharacter(char c){
        if(curRow >= 0 && curRow < NUM_GUESSES && curCol >= 0 && curCol < WORD_LENGTH){
            ((Button)findViewById(tileIds[curRow][curCol])).setText(((Character)c).toString());
            setStyle(tileIds[curRow][curCol], TileStyle.WHITE);
            curCol++;
        }
    }

    /**
     * Removes the last typed character from the tile grid
     */
    public void deleteCharacter(){
        if(curRow >= 0 && curRow < NUM_GUESSES && curCol > 0 && curCol <= WORD_LENGTH){
            curCol--;
            setStyle(tileIds[curRow][curCol], TileStyle.EMPTY);
        }
    }

    /**
     * Locks all the tiles and buttons to prevent further actions
     */
    private void lockAll(){
        findViewById(R.id.advance).setEnabled(false);
        findViewById(R.id.advance).setClickable(false);
        disableKeyboard();
        for(int i = 0; i < NUM_GUESSES; i++){
            for(int j = 0; j < WORD_LENGTH; j++){
                findViewById(tileIds[i][j]).setClickable(false);
            }
        }
    }

    /**
     * Sets the style of the button with the given id to the given style
     * Intended only for user with the main game tiles
     * @param tileId the id of the button to change style
     * @param tileStyle the style to set the button to
     */
    private void setStyle(int tileId, TileStyle tileStyle){

        Button button = findViewById(tileId);

        button.setTag(tileStyle);
        button.setBackground(null);
        switch (tileStyle) {
            case GRAY -> {
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_gray));
            }
            case YELLOW -> {
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_yellow));
            }
            case GREEN -> {
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_green));
            }
            case WHITE -> {
                button.setTextColor(button.getResources().getColor(R.color.text_black));
                button.setBackgroundResource(R.drawable.white_tile_background);
            }
            default -> {
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundResource(R.drawable.empty_tile_background);
                button.setTag(TileStyle.EMPTY);
            }
        }
    }

    /**
     * Gets the current style of the given button
     * NOTE: this only works if the style of the button was set with setStyle or has not been altered
     * @param tileId the id of the button to check style on
     * @return the current style of the button
     */
    private TileStyle getStyle(int tileId){
        Button button = findViewById(tileId);
        return (TileStyle) button.getTag();
    }

    /**
     * Fills the best word to start with into the tile grid as the next 5 characters
     * Assumes that the current row is empty
     */
    private void getFirstWord(){
        String str = wordleSolver.getBestWord();
        for(char c : str.toCharArray()) addCharacter(c);
        enableKeyboard();
    }

    /**
     * Button to form the individual tiles for user input
     */
    private class TileButton implements Button.OnClickListener{
        //location of (this) in the tile grid
        private final int row, col;

        public TileButton(int row, int col){
            this.row = row;
            this.col = col;
        }

        /**
         * Changes the style of (this), rotating between gray, green, and yellow
         * @param view
         */
        public void onClick(View view) {
            int Id = tileIds[row][col];
            TileStyle ts = getStyle(Id);
            switch (ts) {
                case GRAY -> setStyle(Id, TileStyle.YELLOW);
                case YELLOW -> setStyle(Id, TileStyle.GREEN);
                case GREEN -> setStyle(Id, TileStyle.GRAY);
            }
        }
    }

    /**
     * Button with two primary modes that serves to progress the state of the app
     * Imposes a toggling between mode where user is expected to select the color of the tiles (as returned by
     * Wordle) and a mode where the user may edit the word to play
     */
    private class AdvanceButton implements View.OnClickListener{
        //toggle for tracking which mode the button is in
        private boolean advanceMode = false;

        /**
         * If the current tile clues are valid, advances to the next guess, returning the best word to the user
         */
        private void advanceToNextRow(){
            //get constraints from tiles, add constraints to wordleSolver
            TileStyle[] ts = {(TileStyle)findViewById(tileIds[curRow][0]).getTag(),
                    (TileStyle)findViewById(tileIds[curRow][1]).getTag(),
                    (TileStyle)findViewById(tileIds[curRow][2]).getTag(),
                    (TileStyle)findViewById(tileIds[curRow][3]).getTag(),
                    (TileStyle)findViewById(tileIds[curRow][4]).getTag()};
            char[] c = {((Button)findViewById(tileIds[curRow][0])).getText().charAt(0),
                    ((Button)findViewById(tileIds[curRow][1])).getText().charAt(0),
                    ((Button)findViewById(tileIds[curRow][2])).getText().charAt(0),
                    ((Button)findViewById(tileIds[curRow][3])).getText().charAt(0),
                    ((Button)findViewById(tileIds[curRow][4])).getText().charAt(0)};
            if(!wordleSolver.addConstraints(c, ts)) {
                //display alert for contradictory constraints, do nothing else
                alert(getString(R.string.alert_contradiction));
                return;
            }

            //lock tiles
            for(int i = 0; i < WORD_LENGTH; i++)
                findViewById(tileIds[curRow][i]).setClickable(false);

            //check if tiles are all green, if yes, lock/return (game won)
            for(int i = 0; i < WORD_LENGTH; i++){
                if(getStyle(tileIds[curRow][i]) != TileStyle.GREEN) break;
                //loop has not broken; all tiles must be green
                if(i + 1 == WORD_LENGTH) {
                    //lock to prevent clicking
                    lockAll();
                    return;
                }
            }

            //check for no possible answers remaining
            if(wordleSolver.noWords()) {
                //display alert for no possible remaining and lock app
                alert(getString(R.string.alert_none_remaining));
                lockAll();
                return;
            }

            //unlock keyboard, advance row
            curRow++;
            curCol = 0;
            enableKeyboard();

            //get next best word and add it to tiles
            String str = wordleSolver.getBestWord();
            for(char ch : str.toCharArray())
                addCharacter(ch);

            //flip advanceButton state
            ((Button)findViewById(R.id.advance)).setText(R.string.confirm_word);
            advanceMode = false;

            //check if last word is guaranteed the last word, if yes set all greens and lock
            if(wordleSolver.lastWord()){
                //turn all tiles in last row green automatically to indicate guaranteed solved
                for(int i = 0; i < WORD_LENGTH; i++){
                    setStyle(tileIds[curRow][i], TileStyle.GREEN);
                }
                lockAll();
            }
        }

        /**
         * If current word is valid, proceeds to allowing the user to select the color of the tiles returned by Wordle
         */
        private void advanceToTileSelection(){
            //check to make sure that current word is long enough
            if(curCol != WORD_LENGTH) return;

            //retrieve word from tiles
            StringBuilder guessBuilder = new StringBuilder(WORD_LENGTH);
            for(int i = 0; i < WORD_LENGTH; i++) {
                guessBuilder.append(((Button)findViewById(tileIds[curRow][i])).getText().toString());
            }
            String guess = guessBuilder.toString();

            //check word is a valid guess
            if(!wordleSolver.validGuess(guess)){//if invalid guess, alert and cancel
                //display alert for invalid guess
                alert(getString(R.string.alert_invalid_guess));
                return;
            }

            //unlock tiles, lock keyboard
            disableKeyboard();
            for(int i = 0; i < WORD_LENGTH; i++) {
                findViewById(tileIds[curRow][i]).setClickable(true);
                setStyle(tileIds[curRow][i], TileStyle.GRAY);
            }

            //flip state of advance button
            ((Button)findViewById(R.id.advance)).setText(R.string.confirm_tiles);
            advanceMode = true;
        }

        /**
         * Reads in the state of the tiles on the screen and attempts to advance the program state
         */
        public void onClick(View view) {
            //based on advanceMode, this button will do
            //case advanceMode:
            //  the app is currently accepting changes in tile color,
            //  button press should accept the given clues and find/fill in the next word
            //case !advanceMode:
            //  the app is currently accepting changes to the given word,
            //  button press should lock the word choice and move to accepting changes in tile color
            if(advanceMode) advanceToNextRow();
            else advanceToTileSelection();
        }
    }

    /**
     * Button that resets the state of the app to the default state
     */
    private class ResetButton implements  View.OnClickListener{

        /**
         * Resets the state of the app
         */
        public void onClick(View view) {
            curCol = 0;
            curRow = 0;
            for(int i = 0; i < NUM_GUESSES; i++){
                for(int j = 0; j < WORD_LENGTH; j++){
                    setStyle(tileIds[i][j], TileStyle.EMPTY);
                    findViewById(tileIds[i][j]).setClickable(false);
                }
            }
            ((Button)findViewById(R.id.advance)).setText(R.string.confirm_word);
            findViewById(R.id.advance).setEnabled(true);
            findViewById(R.id.advance).setOnClickListener(new AdvanceButton());
            wordleSolver.reset();
            getFirstWord();
        }
    }

    /**
     * Enables the on-screen keyboard, allowing the user to input/edit words
     */
    private void enableKeyboard(){
        //todo
    }

    /**
     * Disables the on-screen keyboard, preventing the user from inputting/editing words
     */
    private void disableKeyboard(){
        //todo
    }
}
