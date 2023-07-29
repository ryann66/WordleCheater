package com.example.wordlecheater

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import kotlin.random.Random

const val WORD_LENGTH: Int = 5
const val NUM_GUESSES: Int = 6

var rowIndex: Int = 0
var colIndex: Int = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init array of tiles
        var tiles = arrayOf(
            arrayOf( findViewById<Button>(R.id.tile00), findViewById<Button>(R.id.tile01),
                findViewById<Button>(R.id.tile02), findViewById<Button>(R.id.tile03),
                findViewById<Button>(R.id.tile04), ),
            arrayOf( findViewById<Button>(R.id.tile10), findViewById<Button>(R.id.tile11),
                findViewById<Button>(R.id.tile12), findViewById<Button>(R.id.tile13),
                findViewById<Button>(R.id.tile14), ),
            arrayOf( findViewById<Button>(R.id.tile20), findViewById<Button>(R.id.tile21),
                findViewById<Button>(R.id.tile22), findViewById<Button>(R.id.tile23),
                findViewById<Button>(R.id.tile24), ),
            arrayOf( findViewById<Button>(R.id.tile30), findViewById<Button>(R.id.tile31),
                findViewById<Button>(R.id.tile32), findViewById<Button>(R.id.tile33),
                findViewById<Button>(R.id.tile34), ),
            arrayOf( findViewById<Button>(R.id.tile40), findViewById<Button>(R.id.tile41),
                findViewById<Button>(R.id.tile42), findViewById<Button>(R.id.tile43),
                findViewById<Button>(R.id.tile44), ),
            arrayOf( findViewById<Button>(R.id.tile50), findViewById<Button>(R.id.tile51),
                findViewById<Button>(R.id.tile52), findViewById<Button>(R.id.tile53),
                findViewById<Button>(R.id.tile54), ),
        )

        findViewById<Button>(R.id.clickMe).setOnClickListener{
            addLetter(tiles, ('A'..'Z').random())
        }
    }

    /**
     * Types the given chararcter, if row is not full
     */
    fun addLetter(tiles: Array<Array<Button>>, c: Char){
        if(colIndex >= WORD_LENGTH) return
        var button = tiles.get(rowIndex).get(colIndex)
        button.setText(c.toString())
        //change button style
        colIndex++
    }
}