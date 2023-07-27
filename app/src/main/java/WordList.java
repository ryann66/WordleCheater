import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class WordList implements Iterable<String>{
    List<String> words;

    public WordList(File wordlist) throws FileNotFoundException {
        Scanner input = new Scanner(wordlist);
        words = new LinkedList<>();
        while(input.hasNextLine()){
            words.add(input.nextLine());
        }
    }

    public Iterator<String> iterator(){
        return new wordIterator(words);
    }

    private class wordIterator implements Iterator<String>{
        private Iterator<String> listIter;

        public wordIterator(Iterable<String> collection){
            listIter = collection.iterator();
        }

        @Override
        public boolean hasNext() {
            return listIter.hasNext();
        }

        @Override
        public String next(){
            return listIter.next();
        }

        @Override
        public void remove(){
            listIter.remove();
        }
    }
}
