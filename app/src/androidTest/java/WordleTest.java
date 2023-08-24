import com.example.wordlecheater.wordleSolver.Wordle;
import com.example.wordlecheater.wordleSolver.TileStyle;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordleTest {
    @Test
    public void testUniform(){
        Wordle wordle = new Wordle("alert");
        //bingo
        Wordle.GuessResult result = wordle.guess("bingo");
        assertArrayEquals(("bingo").toCharArray(), result.cResult);
        for(TileStyle ts : result.tsResult) assertEquals(TileStyle.GRAY, ts);
        //later
        result = wordle.guess("later");
        assertArrayEquals(("later").toCharArray(), result.cResult);
        for(TileStyle ts : result.tsResult) assertEquals(TileStyle.YELLOW, ts);
        //alert
        result = wordle.guess("alert");
        assertArrayEquals(("alert").toCharArray(), result.cResult);
        for(TileStyle ts : result.tsResult) assertEquals(TileStyle.GREEN, ts);
    }

    @Test
    public void testMixed(){
        Wordle wordle = new Wordle("cakes");
        //fleas
        Wordle.GuessResult result = wordle.guess("fleas");
        assertArrayEquals(("fleas").toCharArray(), result.cResult);
        assertEquals(TileStyle.GRAY, result.tsResult[0]);
        assertEquals(TileStyle.GRAY, result.tsResult[1]);
        assertEquals(TileStyle.YELLOW, result.tsResult[2]);
        assertEquals(TileStyle.YELLOW, result.tsResult[3]);
        assertEquals(TileStyle.GREEN, result.tsResult[4]);
    }

    @Test
    public void testDoublesPresent(){
        Wordle wordle;
        Wordle.GuessResult result;

        //double greens
        wordle = new Wordle("banal");
        //basal
        result = wordle.guess("basal");
        assertArrayEquals(("basal").toCharArray(), result.cResult);
        assertEquals(TileStyle.GREEN, result.tsResult[0]);
        assertEquals(TileStyle.GREEN, result.tsResult[1]);
        assertEquals(TileStyle.GRAY, result.tsResult[2]);
        assertEquals(TileStyle.GREEN, result.tsResult[3]);
        assertEquals(TileStyle.GREEN, result.tsResult[4]);

        //one green, one yellow
        wordle = new Wordle("ahead");
        //agora
        result = wordle.guess("agora");
        assertArrayEquals(("agora").toCharArray(), result.cResult);
        assertEquals(TileStyle.GREEN, result.tsResult[0]);
        assertEquals(TileStyle.GRAY, result.tsResult[1]);
        assertEquals(TileStyle.GRAY, result.tsResult[2]);
        assertEquals(TileStyle.GRAY, result.tsResult[3]);
        assertEquals(TileStyle.YELLOW, result.tsResult[4]);

        //one green
        wordle = new Wordle("algae");
        //axles
        result = wordle.guess("axles");
        assertArrayEquals(("axles").toCharArray(), result.cResult);
        assertEquals(TileStyle.GREEN, result.tsResult[0]);
        assertEquals(TileStyle.GRAY, result.tsResult[1]);
        assertEquals(TileStyle.YELLOW, result.tsResult[2]);
        assertEquals(TileStyle.YELLOW, result.tsResult[3]);
        assertEquals(TileStyle.GRAY, result.tsResult[4]);

        //one yellow
        wordle = new Wordle("ozone");
        //youth
        result = wordle.guess("youth");
        assertArrayEquals(("youth").toCharArray(), result.cResult);
        assertEquals(TileStyle.GRAY, result.tsResult[0]);
        assertEquals(TileStyle.YELLOW, result.tsResult[1]);
        assertEquals(TileStyle.GRAY, result.tsResult[2]);
        assertEquals(TileStyle.GRAY, result.tsResult[3]);
        assertEquals(TileStyle.GRAY, result.tsResult[4]);
    }

    @Test
    public void testDoublesAbsent(){
        Wordle wordle;
        Wordle.GuessResult result;

        wordle = new Wordle("thing");
        //which
        result = wordle.guess("which");
        assertArrayEquals(("which").toCharArray(), result.cResult);
        assertEquals(TileStyle.GRAY, result.tsResult[0]);
        assertEquals(TileStyle.GREEN, result.tsResult[1]);
        assertEquals(TileStyle.GREEN, result.tsResult[2]);
        assertEquals(TileStyle.GRAY, result.tsResult[3]);
        assertEquals(TileStyle.GRAY, result.tsResult[4]);
    }

    //tests present and absent with triples to a limited extent
    @Test
    public void testTriples(){
        Wordle wordle;
        Wordle.GuessResult result;

        wordle = new Wordle("mommy");
        //momma
        result = wordle.guess("momma");
        assertArrayEquals(("momma").toCharArray(), result.cResult);
        assertEquals(TileStyle.GREEN, result.tsResult[0]);
        assertEquals(TileStyle.GREEN, result.tsResult[1]);
        assertEquals(TileStyle.GREEN, result.tsResult[2]);
        assertEquals(TileStyle.GREEN, result.tsResult[3]);
        assertEquals(TileStyle.GRAY, result.tsResult[4]);

        wordle = new Wordle("levee");
        //geese
        result = wordle.guess("geese");
        assertArrayEquals(("geese").toCharArray(), result.cResult);
        assertEquals(TileStyle.GRAY, result.tsResult[0]);
        assertEquals(TileStyle.GREEN, result.tsResult[1]);
        assertEquals(TileStyle.YELLOW, result.tsResult[2]);
        assertEquals(TileStyle.GRAY, result.tsResult[3]);
        assertEquals(TileStyle.GREEN, result.tsResult[4]);

        wordle = new Wordle("ruler");
        //rarer
        result = wordle.guess("rarer");
        assertArrayEquals(("rarer").toCharArray(), result.cResult);
        assertEquals(TileStyle.GREEN, result.tsResult[0]);
        assertEquals(TileStyle.GRAY, result.tsResult[1]);
        assertEquals(TileStyle.GRAY, result.tsResult[2]);
        assertEquals(TileStyle.GREEN, result.tsResult[3]);
        assertEquals(TileStyle.GREEN, result.tsResult[4]);
    }
}
