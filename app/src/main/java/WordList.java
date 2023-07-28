import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WordList{
    public static final int wordLength = 5;

    List<String> words;

    public WordList(File wordlist) throws FileNotFoundException {
        Scanner input = new Scanner(wordlist);
        words = new LinkedList<>();
        while(input.hasNextLine()){
            words.add(input.nextLine());
        }
    }

    /**
     * Removes any invalid words from the word list
     * @param constraint the constraint to be placed upon all words in the word list
     * @throws IllegalStateException if the word list is empty afterwards
     */
    public void addConstraint(Constraint constraint) throws IllegalStateException{
        Iterator<String> iter = words.iterator();
        while(iter.hasNext()){
            if(constraint.invalid(iter.next())) iter.remove();
        }
        if(words.isEmpty()) throw new IllegalStateException("Empty word list");
    }

    /**
     * Returns the best word that can be played
     * @return the best word that can be played
     * @throws IllegalStateException if the wordlist is empty
     */
    public String getBestWord(){
        if(words.isEmpty()) throw new IllegalStateException();
        //TODO: fancy algorithm
        return null;
    }
}
