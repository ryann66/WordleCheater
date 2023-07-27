import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

public class WordleSolver{
    private WordList wordList;

    public WordleSolver(boolean cheatMode) throws FileNotFoundException {
        File wordFile;
        if(cheatMode) wordFile = new File("validwords.txt");
        else wordFile = new File("allwords.txt");
        wordList = new WordList(wordFile);
    }

    /**
     * returns the best word to play
     * @return
     */
    public String getBestWord(){
        return wordList.getBestWord();
    }

    /**
     * applys the given constraints to the word list
     * @param constraints collection of constraints to add
     * @return true if the word list is not empty (i.e. there is a word that can still be selected)
     */
    public boolean processInput(Collection<Constraint> constraints){
        try{
            for(Constraint c : constraints)
                wordList.addConstraint(c);
        }catch(IllegalStateException ise){
            return false;
        }
        return true;
    }
}