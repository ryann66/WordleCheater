package com.example.wordlecheater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.ComponentActivity;
import java.io.IOException;

public class MainActivity extends ComponentActivity {

    enum TileStyle{
        EMPTY, WHITE, GRAY, YELLOW, GREEN
    }

    //changing these will cause the program to not function, particularly UI elements
    public static final int NUM_GUESSES = 6, WORD_LENGTH = 5;

    int[][] tileIds;
    int curRow = 0, curCol = 0;
    private WordleSolver wordleSolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            wordleSolver = new RandomSolver(getAssets());
        }catch(IOException ioe){
            System.exit(1);
        }

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
     * Types the chararcter into the tile grid
     * @param c
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

    private void setStyle(int tileId, TileStyle tileStyle){

        Button button = findViewById(tileId);

        button.setTag(tileStyle);
        button.setBackground(null);
        switch (tileStyle){
            case GRAY:
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_gray));
                break;
            case YELLOW:
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_yellow));
                break;
            case GREEN:
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundColor(button.getResources().getColor(R.color.select_green));
                break;
            case WHITE:
                button.setTextColor(button.getResources().getColor(R.color.text_black));
                button.setBackgroundResource(R.drawable.white_tile_background);
                break;
            default:
                button.setTextColor(button.getResources().getColor(R.color.text_white));
                button.setBackgroundResource(R.drawable.empty_tile_background);
                button.setTag(TileStyle.EMPTY);
        }
    }

    private TileStyle getStyle(int tileId){
        Button button = findViewById(tileId);
        return (TileStyle) button.getTag();
    }

    private void getFirstWord(){
        String str = wordleSolver.getBestWord();
        for(char c : str.toCharArray()) addCharacter(c);
        enableKeyboard();
    }

    private class TileButton implements Button.OnClickListener{

        private int row, col;

        public TileButton(int row, int col){
            this.row = row;
            this.col = col;
        }

        public void onClick(View view) {
            int Id = tileIds[row][col];
            TileStyle ts = getStyle(Id);
            switch (ts){
                case GRAY: setStyle(Id, TileStyle.YELLOW);
                break;
                case YELLOW: setStyle(Id, TileStyle.GREEN);
                break;
                case GREEN: setStyle(Id, TileStyle.GRAY);
                break;
            }
        }
    }

    private class AdvanceButton implements View.OnClickListener{
        private boolean advanceMode = false;
        public void onClick(View view) {
            if(curCol != WORD_LENGTH){
                //do nothing
                return;
            }
            if(advanceMode){
                //add constraints to wordleSolver, if fails then return
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
                    //todo display alert for contradictory constraints
                    return;
                }
                //lock tiles, unlock keyboard, advance row, get next word
                for(int i = 0; i < WORD_LENGTH; i++)
                    findViewById(tileIds[curRow][i]).setClickable(false);
                curRow++;
                curCol = 0;
                if(!wordleSolver.noWords()){//if no words, fall through and get caught by lock at method end
                    enableKeyboard();
                    String str = wordleSolver.getBestWord();
                    for(char ch : str.toCharArray())
                        addCharacter(ch);
                    ((Button)findViewById(R.id.advance)).setText("Confirm word");
                    advanceMode = false;
                }
            }
            else{
                String guess = "";
                for(int i = 0; i < WORD_LENGTH; i++) {
                    guess += ((Button)findViewById(tileIds[curRow][i])).getText().toString();
                }
                if(!wordleSolver.validGuess(guess)){//if invalid guess, alert and cancel
                    //todo display alert for invalid guess
                    return;
                }
                //unlock tiles, lock keyboard
                disableKeyboard();
                for(int i = 0; i < WORD_LENGTH; i++) {
                    findViewById(tileIds[curRow][i]).setClickable(true);
                    setStyle(tileIds[curRow][i], TileStyle.GRAY);
                }
                ((Button)findViewById(R.id.advance)).setText("Confirm tile colors");
                advanceMode = true;
            }
            if(curRow == NUM_GUESSES || wordleSolver.lastWord() || wordleSolver.noWords()){
                //lock to prevent clicking
                findViewById(R.id.advance).setEnabled(false);
                findViewById(R.id.advance).setClickable(false);
                disableKeyboard();
                for(int i = 0; i < NUM_GUESSES; i++){
                    for(int j = 0; j < WORD_LENGTH; j++){
                        findViewById(tileIds[i][j]).setClickable(false);
                    }
                }
            }
        }
    }

    private class ResetButton implements  View.OnClickListener{

        public void onClick(View view) {
            curCol = 0;
            curRow = 0;
            for(int i = 0; i < NUM_GUESSES; i++){
                for(int j = 0; j < WORD_LENGTH; j++){
                    setStyle(tileIds[i][j], TileStyle.EMPTY);
                    findViewById(tileIds[i][j]).setClickable(false);
                }
            }
            ((Button)findViewById(R.id.advance)).setText("Confirm word");
            findViewById(R.id.advance).setEnabled(true);
            findViewById(R.id.advance).setOnClickListener(new AdvanceButton());
            wordleSolver.reset();
            getFirstWord();
        }
    }

    private void enableKeyboard(){

    }

    private void disableKeyboard(){

    }
}
