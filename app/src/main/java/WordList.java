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

    public void addConstraint(Constraint constraint) {

    }
}
