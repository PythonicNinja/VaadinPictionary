package pl.Wojtek.model;

/**
 *
 */
public class Word {

    private String word;



    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Word(" + word + ")";
    }
}
