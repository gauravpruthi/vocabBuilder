//Word Record is a single word record in list the list will be built up 
//using all word records. So, it happens this way that parsing is done
//on file and for each word a string is added to queue of syn and ant
//as in th case of DFS, no need of flag since word is checked

package vocabulary;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Gaurav Pruthi
 * This class represents a word object that includes its meaning
 * its synonyms, antonyms and a description or example usage
 */

public class WordRecord {
    private String word;
    private String meaning;
    ConcurrentLinkedQueue<String>  syn= new ConcurrentLinkedQueue< String >();
    ConcurrentLinkedQueue<String> ant =  new ConcurrentLinkedQueue< String >();
    private String usage;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public ConcurrentLinkedQueue< String > getSyn() {
        return syn;
        
    }

    public void setSyn(ConcurrentLinkedQueue< String > syn) {
        this.syn = syn;
    }

    public ConcurrentLinkedQueue<String> getAnt() {
        return ant;
    }

    public void setAnt(ConcurrentLinkedQueue<String> ant) {
        this.ant = ant;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
    
}
