import java.util.Objects;

public class Constraint{
    public final char character;
    public final int index;
    public final Color color;

    public enum Color{
        GRAY,
        YELLOW,
        GREEN
    }

    /**
     * Creates a new constraint
     * @param character the alphabetical character that new knowledge about is known
     * @param index the index the character is at
     * @param color the color of the tile
     */
    public Constraint(char character, int index, Color color){
        if(color == null || index < 0 || index >= WordList.wordLength || !Character.isAlphabetic(character))
            throw new IllegalArgumentException("Bad constraint arguments");
        this.character = character;
        this.index = index;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return character == that.character && index == that.index && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, index, color);
    }

    /**
     * Checks if the given word is valid under this constraint
     * Assumed word is of correct length
     * @param word the word to check against
     * @return
     */
    public boolean invalid(String word){
        char[] wordChars = word.toCharArray();
        switch (this.color){
            case GREEN:
                if(wordChars[this.index] == this.character) return false;
                return true;
            case YELLOW:
                if(wordChars[this.index] == this.character) return true;
                for(int i = 0; i < wordChars.length; i++){
                    if(wordChars[i] == this.character) return false;
                }
                return true;
            default://gray
                for(int i = 0; i < wordChars.length; i++){
                    if(wordChars[i] == this.character) return true;
                }
                return false;
        }
    }
}