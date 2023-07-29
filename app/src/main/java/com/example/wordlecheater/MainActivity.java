package com.example.wordlecheater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.ComponentActivity;

public class MainActivity extends ComponentActivity {

    static <T> T[] arrayOf(T... args){
        return args;
    }

    //changing these will cause the program to not function, particularly UI elements
    static final int NUM_GUESSES = 6, WORD_LENGTH = 5;

    TileButton[][] tiles = new TileButton[NUM_GUESSES][WORD_LENGTH];
    int curRow = 0, curCol = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init tiles arrays
        View[][] buttons = new View[][]{
            {findViewById(R.id.tile00), findViewById(R.id.tile01), findViewById(R.id.tile02),
                findViewById(R.id.tile03), findViewById(R.id.tile04)},
            {findViewById(R.id.tile10), findViewById(R.id.tile11), findViewById(R.id.tile12),
                findViewById(R.id.tile13), findViewById(R.id.tile14)},
            {findViewById(R.id.tile20), findViewById(R.id.tile21), findViewById(R.id.tile22),
                findViewById(R.id.tile23), findViewById(R.id.tile24)},
            {findViewById(R.id.tile30), findViewById(R.id.tile31), findViewById(R.id.tile32),
                findViewById(R.id.tile33), findViewById(R.id.tile34)},
            {findViewById(R.id.tile40), findViewById(R.id.tile41), findViewById(R.id.tile42),
                findViewById(R.id.tile43), findViewById(R.id.tile44)},
            {findViewById(R.id.tile50), findViewById(R.id.tile51), findViewById(R.id.tile52),
                findViewById(R.id.tile53), findViewById(R.id.tile54)},
        };
        //create tileButtons array, set onclicklisteners (in constructor)
        try{
            for(int i = 0; i < NUM_GUESSES; i++){
                for(int j = 0; j < WORD_LENGTH; j++){
                    tiles[i][j] = new TileButton((Button)buttons[i][j]);
                }
            }
        }catch(ClassCastException cce){
            System.exit(1);
        }

        //add other buttons to onclicklisteners
    }
}
