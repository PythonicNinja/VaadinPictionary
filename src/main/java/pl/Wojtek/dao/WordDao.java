package pl.Wojtek.dao;

import pl.Wojtek.data.WordContainer;
import pl.Wojtek.model.Word;

import java.util.List;

/**
 *
 */
public class WordDao {

    private WordContainer wordContainer = WordContainer.getInstance();


    public List<Word> getWords(){
        return wordContainer.getWords();
    }

}
