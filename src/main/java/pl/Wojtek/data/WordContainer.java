package pl.Wojtek.data;

import pl.Wojtek.model.Room;
import pl.Wojtek.model.Word;

import java.util.*;

public class WordContainer {

    private static List<Word> words = Collections.synchronizedList(new ArrayList<Word>());
    private final static Object dataLock = new Object();

    /*
     * Thread safe singleton class
     * http://stackoverflow.com/a/16106598/2170846
     */
    private static class DataContainerHolder{
        static final WordContainer WORD_CONTAINER_INSTANCE = new WordContainer();
    }

    public static WordContainer getInstance(){
        return DataContainerHolder.WORD_CONTAINER_INSTANCE;
    }

    private WordContainer(){
        initial();
    }

    public List<Word> getWords(){
        synchronized (dataLock) {
            return words;
        }
    }

    public void addWord(Word w){
        synchronized (dataLock) {
            words.add(w);
        }
    }

    public Word getRandomWord(){
        synchronized (dataLock) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(words.size());
            return words.get(index);
        }
    }

    public void initial(){
        ArrayList<String> wordList = new ArrayList<String>() {{
            add("ala");
            add("ma");
            add("kota");
        }};
        for (String wordName : wordList) {
            Word word = new Word();
            word.setWord(wordName);
            this.addWord(word);
        }
    }
}
